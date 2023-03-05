package com.github.dissemination.moment.domain;

import com.github.content.feed.exception.FeedException;
import com.github.content.feed.infra.UserFacade;
import com.github.content.feed.infra.model.UserInfo;
import com.github.dissemination.mine.domain.MineContentDomainService;
import com.github.dissemination.mine.infra.FriendFacade;
import com.github.dissemination.moment.app.model.PatchMomentArgument;
import com.github.dissemination.moment.domain.config.MomentConfig;
import com.github.dissemination.moment.domain.repo.MomentRepository;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 朋友圈修补器 -> 用户朋友圈的数据可能是存在缺漏的，可通过修补器材进行修补一个时间点之后的数据.
 *
 * @author yanghuan
 */
@Service
public class MomentPatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MomentPatcher.class);

    private static ExecutorService executorService = null;

    @Autowired
    private MomentDomainService momentDomainService;

    @Autowired
    private MineContentDomainService mineContentDomainService;

    @Autowired
    private FriendFacade friendFacade;

    @Autowired
    private MomentRepository momentRepository;

    @Autowired
    private UserFacade userFacade;

    /**
     * 修补用户朋友圈
     * @param argument 刷新朋友圈参数
     */
    public void patch(PatchMomentArgument argument) {
        if(argument == null || StringUtils.isEmpty(argument.getUser())) {
            throw new FeedException();
        }

        String uid = argument.getUser();
        Date userRegisterTime = null;
        UserInfo userInfo = userFacade.getUserInfo(uid);
        String registerTimeStr = "";
        try {
            // 刚注册十分钟的不同步任何feed
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userRegisterTime = simpleDateFormat.parse(registerTimeStr);
            boolean isNewUser = System.currentTimeMillis() - userRegisterTime.getTime() < TimeUnit.MINUTES.toMillis(10);
            if(isNewUser) {
                return;
            }
        }
        catch (Exception e) {
        }

        List<String> pullFriends = new ArrayList<>();

        // 刷新用户朋友圈必须刷新官方账号的动态
        Set<String> officialAccounts = MomentConfig.get().getOfficialAccounts();
        if(CollectionUtils.isNotEmpty(officialAccounts)) {
            pullFriends.addAll(officialAccounts);
        }

        Long refreshAfterThisTime = argument.getRefreshAfterThisTime();
        // 1.刷新时间点为空那么刷新所有用户所有feed
        // 2.需要刷新的起始点正好在活跃用户的判定时间内，朋友圈内容是完整的，正好不需要刷新好友动态
        // 3.策略之后进行扩展，目前存在一定的局限
        if(refreshAfterThisTime == null || System.currentTimeMillis() - refreshAfterThisTime >= TimeUnit.DAYS.toMillis(MomentConfig.get().getActiveUserLoginDurationDay())) {
            List<String> friends = friendFacade.getUserFriends(uid);
            if(CollectionUtils.isNotEmpty(friends)) {
                pullFriends.addAll(friends);
            }
        }

        if(CollectionUtils.isEmpty(pullFriends)) {
            return;
        }

        pullFriends = pullFriends.stream().distinct().collect(Collectors.toList());

        List<List<String>> partitions = Lists.partition(pullFriends, 10);
        List<CompletableFuture> futures = new ArrayList<>();
        for(List<String> partition : partitions) {
            Date finalUserRegisterTime = userRegisterTime;
            CompletableFuture future = CompletableFuture.runAsync(() -> {
                for(String friendUid : partition) {
                    try {
                        this.copyFriendFeedToMe(uid, finalUserRegisterTime, friendUid);
                    }
                    catch (Exception e) {
                    }
                }
            }, executorService);

            futures.add(future);
        }

        CompletableFuture<Void> finalFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {}));
        try {
            finalFuture.get(100, TimeUnit.MILLISECONDS);
        }
        catch (Exception e) {
        }

        LOGGER.info("act=patchUserMoment end argument={}", argument);
    }

    /**
     * 把好友的feed同步到我的收件箱
     * @param uid 用户ID
     * @param friendUid 朋友UID
     */
    private void copyFriendFeedToMe(String uid, Date userRegisterTime, String friendUid) {
        if(StringUtils.isEmpty(uid) || StringUtils.isEmpty(friendUid)) {
            throw new FeedException();
        }

        if(StringUtils.equals(uid, friendUid)) {
            throw new FeedException();
        }

//        Set<String> abandonMomentUids = MomentConfig.get().getAbandonMomentUids();
//        if(CollectionUtils.isNotEmpty(abandonMomentUids) && abandonMomentUids.contains(friendUid)) {
//            LOGGER.info("act=copyFriendFeedToMe is abandonMomentUser uid={} friendUid={}", uid, friendUid);
//            return;
//        }
//
//        Long minFeedId = momentDomainService.getUserMomentMinFeedId(uid);
//        minFeedId = minFeedId == null ? 0L : minFeedId;
//
//        Long latestFeedId = profileDomainService.getUserLatestFeedId(friendUid);
//        latestFeedId = latestFeedId == null ? 0L : latestFeedId;
//
//        // 都比用户朋友圈里最老的feed都老了，无需同步了，认为是已经被清理的了
//        if(latestFeedId <= minFeedId) {
//            LOGGER.info("act=copyFriendFeedToMe latestFeedId <= minFeedId uid={} friendUid={} minFeedId={}", uid, friendUid, minFeedId);
//            return;
//        }
//
//        List<Long> friendFeeds = profileDomainService.queryUserProfileFeeds(IdentityType.FRIEND, friendUid, Long.MAX_VALUE, MomentConfig.get().getMaxCopyOneFriendFeedSize());
//        if(CollectionUtils.isEmpty(friendFeeds)) {
//            LOGGER.info("act=copyFriendFeedToMe fail feeds isEmpty uid={} friendUid={} minFeedId={}", uid, friendUid, minFeedId);
//            return;
//        }
//
//        // 通过 friendMaxFeedId 来判定是否同步过，可能会少同步feed的情况
//        // 例如：当feedID为10和feedID为9的同时同步用户A的收件箱，当feedID为10的先同步进去了，feedID为9的同步时发现用户A正好不是活跃用户了，所以feedID为9没有同步进去
//        // todo 存在一个极限情况，用户加好友后立即发了个feed，可能导致这些需要同步的老feed同步不进去，但是对执行效率做了取舍，所以这样处理
//        Long friendMaxFeedId = momentDomainService.getMomentFriendMaxFeedId(uid, friendUid);
//        if(friendMaxFeedId != null && friendFeeds.get(0) <= friendMaxFeedId) {
//            LOGGER.info("act=copyFriendFeedToMe fail friendFeeds.get(0) <= friendMaxFeedId uid={} friendUid={} friendMaxFeedId={}", uid, friendUid, friendMaxFeedId);
//            return;
//        }
//
//        for(Long feedId : friendFeeds) {
//            if(feedId == null) {
//                continue;
//            }
//
//            if(friendMaxFeedId != null && feedId <= friendMaxFeedId) {
//                continue;
//            }
//
//            FeedSourceModel feed = contentService.getFeed(feedId);
//            if(feed == null) {
//                continue;
//            }
//
//            if(feed.getAnonymousFeedType() == FeedAnonymousEnum.ANONYMOUS.getCode()) {
//                continue;
//            }
//
//            if(feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.FEED_SELF_VIEW && feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.UGC_SELF_VIEW) {
//                continue;
//            }
//
//            if(feed.getPrivilege() != FeedPrivilegeEnum.FRIEND_VIEW && feed.getPrivilege() != FeedPrivilegeEnum.SQUARE_VIEW) {
//                continue;
//            }
//
//            // feed时间小于用户注册时间，不需要同步了
//            if(userRegisterTime != null && feed.getFeedCreateTime() != null) {
//                if(feed.getFeedCreateTime().before(userRegisterTime)) {
//                    continue;
//                }
//            }
//
//            MomentRecordDO momentRecord = new MomentRecordDO();
//            momentRecord.setUid(uid);
//            momentRecord.setFeedId(feed.getFeedId());
//            momentRecord.setFeedOwnerId(feed.getUid());
//            momentRecord.setFeedCreateTime(feed.getFeedCreateTime());
//            momentDataLogic.saveMomentRecord(momentRecord);
//        }

//        LOGGER.info("act=copyFriendFeedToMe feeds uid={} friendUid={} friendMaxFeedId={}", uid, friendUid, friendMaxFeedId);
    }
}
