package com.github.dissemination.moment.app;

import com.github.content.feed.app.FeedService;
import com.github.content.feed.domain.model.FeedModel;
import com.github.content.feed.exception.FeedException;
import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.dissemination.moment.app.model.EnterAppInfo;
import com.github.dissemination.moment.app.model.PatchMomentArgument;
import com.github.dissemination.moment.domain.MomentDomainService;
import com.github.dissemination.moment.domain.MomentPatcher;
import com.github.dissemination.moment.infra.MsgFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 基于feed的朋友圈业务.
 *
 * @author yanghuan
 */
@Service
public class FeedMomentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedMomentService.class);

    private static ExecutorService executorService = null;

    @Autowired
    private MomentDomainService momentDomainService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private MomentPatcher momentPatcher;

    @Autowired
    private MsgFacade msgFacade;

    /**
     * 缓存用户浏览过的最新feed
     */
    private static final String USER_MOMENT_FEED = "user:moment:feed:";

    private static final int EXPIRE_TIME = (int) TimeUnit.DAYS.toSeconds(15);

    /**
     * 浏览朋友圈动态.
     */
    public List<Long> queryMomentFeeds(String uid, Long lastFeedId) {
        if(StringUtils.isEmpty(uid)) {
            throw new FeedException();
        }

        List<Long> feeds = momentDomainService.queryFeeds(uid, ContentCarrierTypeEnum.FEED, lastFeedId);

        if(lastFeedId == null || lastFeedId == 0) {
            if(CollectionUtils.isNotEmpty(feeds)) {
                // 记录用户已浏览的最大feedID
                executorService.execute(() -> {
                    this.updateUserSeenMaxFeedId(uid, feeds.get(0));
                });
            }
        }

        LOGGER.info("act=momentQueryMomentFeeds uid={} lastFeedId={}", uid, lastFeedId);
        return feeds;
    }

    /**
     * 用户发布动态.
     */
    public void publishFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        FeedModel feed = feedService.getFeed(feedId);
        if(feed == null) {
            throw new FeedException();
        }

        momentDomainService.distributeContentToFriends(feed.getFeedUid(), feed.getCarrierType(), feed.getCarrierId());

        LOGGER.info("act=momentPublishFeed feedId={}", feedId);
    }

    /**
     * 推送动态给自己.
     */
    public void distributeFeedToSelf(Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        FeedModel feed = feedService.getFeed(feedId);
        if(feed == null) {
            throw new FeedException();
        }

        momentDomainService.distributeContentToSelf(feed.getFeedUid(), feed.getCarrierType(), feed.getCarrierId());

        LOGGER.info("act=momentDistributeFeedToSelf feedId={}", feedId);
    }

    /**
     * 用户删除动态.
     */
    public void deleteFeed(String uid, Long feedId) {
        if(StringUtils.isEmpty(uid) || feedId == null) {
            throw new FeedException();
        }

        momentDomainService.deleteContent(uid, ContentCarrierTypeEnum.FEED, feedId);

        LOGGER.info("act=momentDeleteFeed feedId={}", feedId);
    }

    /**
     * 解除好友关系.
     */
    public void dissolveFriend(String uid, String friendUid) {
        if(StringUtils.isEmpty(uid) || StringUtils.isEmpty(friendUid)) {
            throw new FeedException();
        }

        // 互删
        momentDomainService.deleteFriendFeeds(uid, friendUid);
        momentDomainService.deleteFriendFeeds(friendUid, uid);

        LOGGER.info("act=momentDissolveFriend uid={} friendUid={}", uid, friendUid);
    }

    /**
     * 用户进入app.
     */
    public void enterApp(EnterAppInfo enterAppInfo) {
        if(enterAppInfo == null || StringUtils.isEmpty(enterAppInfo.getUid())) {
            throw new FeedException();
        }

        String uid = enterAppInfo.getUid();

        PatchMomentArgument argument = new PatchMomentArgument();
        argument.setUser(uid);
        argument.setRefreshAfterThisTime(enterAppInfo.getLastLoginTime());
        momentPatcher.patch(argument);

        List<Long> feedIds = momentDomainService.queryFeeds(uid, ContentCarrierTypeEnum.FEED, 0L);
        if(CollectionUtils.isEmpty(feedIds)) {
            return;
        }

        List<FeedModel> feeds = feedService.batchGetFeeds(feedIds);
        Long userSeenMaxFeedId = getUserSeenMaxFeedId(uid);
        for(FeedModel feed : feeds) {
            if(feed == null) {
                continue;
            }

            if(userSeenMaxFeedId == null || userSeenMaxFeedId < feed.getFeedId()) {
                // 红点通知
                msgFacade.sendSystemMsg();
                break;
            }
        }

        LOGGER.info("act=momentEnterApp end");
    }

    /**
     * 更新用户用户浏览过的最大feed ID.
     */
    private void updateUserSeenMaxFeedId(String uid, Long feedId) {
        if(feedId == null || StringUtils.isEmpty(uid)) {
            return;
        }
    }

    /**
     * 获取用户浏览过的最大feed ID.
     */
    private Long getUserSeenMaxFeedId(String uid) {
        return null;
    }
}
