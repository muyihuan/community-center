package com.github.dissemination.moment.domain.repo;

import com.github.content.feed.exception.FeedException;
import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.dissemination.moment.domain.repo.model.MomentRecordDO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 朋友圈数据存储/计算/缓存处理.
 *
 * @author yanghuan
 */
public interface MomentRepository {

    Logger LOGGER = LoggerFactory.getLogger(MomentRepository.class);

    ExecutorService executorService = null;

    /**
     * 朋友圈只混存第页数据，因为每天访问朋友圈中访问第一页的占80%多，第一页缓存20条
     */
    String ACTIVE_MOMENTS = "moments:feed:list:";

    /**
     * 每个用户每天平均会访问朋友圈10次，所用户访问第一次就几乎很有可能当天再访问第二次。
     * 所以缓存可以采取每次用户访问朋友圈，朋友圈缓存过期时间就累加一天，保证访问频繁的用户的缓存命中率
     */
    int EXPIRE_TIME = (int) TimeUnit.DAYS.toSeconds(1);

    /**
     * 获取朋友圈动态.
     *
     * @param uid 用户ID.
     * @param carrierType 内容载体类型.
     * @param lastId 分页使用.
     * @param count 获取数量.
     * @return 动态列表.
     */
    default List<Long> queryMomentContents(String uid, ContentCarrierTypeEnum carrierType, Long lastId, Integer count) {
        if(StringUtils.isEmpty(uid) || count == null) {
            throw new FeedException();
        }


        return Collections.emptyList();
    }

    /**
     * 保存到用户朋友圈.
     *
     * @param momentRecord 朋友圈动态信息.
     */
    default void saveMomentRecord(MomentRecordDO momentRecord) {
        if(momentRecord == null) {
            throw new FeedException();
        }

        // 数据一致校验和处理.
        {
            executorService.submit(() -> {
//                try {
//                    // 1.内容已删除
//                    Long feedId = momentRecord.getFeedId();
//                    if(feed == null) {
//                        // feed已删除
//                        this.remMomentRecord(momentRecord.getUid(), feedId);
//                        return;
//                    }
//
//                    boolean isSelf = StringUtils.equals(momentRecord.getUid(), momentRecord.getFeedOwnerId());
//                    if(isSelf) {
//                        // 本人没问题
//                        return;
//                    }
//
//                    if(feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.UGC_SELF_VIEW || feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.FEED_SELF_VIEW) {
//                        // 自见不是本人，发生不一致，需要删除
//                        this.remMomentRecord(momentRecord.getUid(), feedId);
//                        return;
//                    }
//
//                    boolean isFriend = userFriendFacade.isFriend(momentRecord.getFeedOwnerId(), momentRecord.getUid());
//                    if(!isFriend) {
//                        // 不是好友关系了，发生不一致，需要删除
//                        this.remMomentRecord(momentRecord.getUid(), feedId);
//                        LOGGER.info("act=dataConsistencyCheck saveMomentRecord msg=不是好友关系了 momentRecord={}");
//                    }
//                }
//                catch (Exception e) {
//                    LOGGER.error("act=momentDataConsistencyCheck error", e);
//                }
            });
        }
    }

    /**
     * 从好友收件箱删除该动态.
     *
     * @param friendUid 朋友UID.
     * @param carrierType 内容载体类型.
     * @param contentCarrierId 内容ID.
     */
    default void remMomentRecord(String friendUid, ContentCarrierTypeEnum carrierType, Long contentCarrierId) {
        if(StringUtils.isEmpty(friendUid) || carrierType == null || contentCarrierId == null) {
            throw new FeedException();
        }

        // 数据一致校验和处理
        {
            executorService.submit(() -> {
//                try {
//                    FeedSourceModel feed = feedContentFacade.getFeed(feedId);
//                    // feed已删除 符合预期
//                    if(feed == null) {
//                        return;
//                    }
//
//                    boolean isSelf = StringUtils.equals(feed.getUid(), friendUid);
//
//                    // 如果自见feed 且 非作者本人 符合预期
//                    if(feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.UGC_SELF_VIEW || feed.getSystemPrivilege() == FeedSystemPrivilegeEnum.FEED_SELF_VIEW) {
//                        // 非作者本人 符合预期
//                        if(!isSelf) {
//                            return;
//                        }
//                    }
//
//                    // 如果不是本人 且 不是好友关系了 符合预期
//                    if(!isSelf) {
//                        boolean isFriend = userFriendFacade.isFriend(feed.getUid(), friendUid);
//                        if(!isFriend) {
//                            return;
//                        }
//                    }
//
//                    MomentRecordDO momentRecord = new MomentRecordDO();
//                    momentRecord.setFeedId(feedId);
//                    momentRecord.setUid(friendUid);
//                    momentRecord.setFeedOwnerId(feed.getUid());
//                    momentRecord.setFeedCreateTime(feed.getFeedCreateTime());
//                    this.saveMomentRecord(momentRecord);
//
//                    LOGGER.info("act=dataConsistencyCheck remMomentRecord msg=删除发生数据不一致 friendUid={} feedId={} isSelf={}", friendUid, feedId, isSelf);
//                }
//                catch (Exception e) {
//                }
            });
        }
    }

    /**
     * 把好友的动态从我的朋友圈里删除.
     *
     * @param uid 用户ID.
     * @param friendUid 朋友UID.
     */
    default void delFriendContentFromMe(String uid, String friendUid) {
        if(StringUtils.equals(uid, friendUid)) {
            throw new FeedException();
        }

        // 数据一致校验和处理
        {
            executorService.submit(() -> {
//                try {
//                    boolean isFriend = userFriendFacade.isFriend(uid, friendUid);
//                    if(!isFriend) {
//                        return;
//                    }
//
//                    this.copyFriendFeedToMe(uid, friendUid);
//
//                    LOGGER.info("act=dataConsistencyCheck delFriendFeedFromMe msg=删除好友feed发生数据不一致 uid={} friendUid={}", uid, friendUid);
//                }
//                catch (Exception e) {
//                }
            });
        }
    }

    /**
     * 删除朋友圈冗余的动态.
     *
     * @param uid 用户ID.
     * @param momentThreshold 朋友圈动态数量的阈值.
     */
    default void cleanOverage(String uid, Integer momentThreshold) {
        if(StringUtils.isEmpty(uid) || momentThreshold == null || momentThreshold <= 0) {
            throw new FeedException();
        }

        LOGGER.info("act=cleanOverage uid={} momentThreshold={}", uid, momentThreshold);
    }

    /**
     * 该用户的收件箱是否存在该feed
     */
    boolean existFeed(String uid, ContentCarrierTypeEnum carrierType, Long contentCarrierId);

    /**
     * 获取用户朋友圈最老的动态.
     *
     * @return 最老的动态ID.
     */
    default Long getOldestContent(String uid) {
        if(StringUtils.isEmpty(uid)) {
            throw new FeedException();
        }

        return null;
    }

    /**
     * 把好友的动态同步到我的朋友圈.
     *
     * @param uid 用户ID.
     * @param friendUid 朋友UID.
     */
    default void copyFriendFeedToMe(String uid, String friendUid) {
        if(StringUtils.isEmpty(uid) || StringUtils.isEmpty(friendUid)) {
            throw new FeedException();
        }

        if(StringUtils.equals(uid, friendUid)) {
            throw new FeedException();
        }

//        List<Long> friendFeeds = getSenderFeeds(friendUid, friendUid, MomentConfig.get().getMomentFeedsThreshold());
//        if(CollectionUtils.isEmpty(friendFeeds)) {
//            LOGGER.info("act=copyFriendFeedToMe friendFeeds isEmpty uid={} friendUid={}", uid, friendUid);
//            return;
//        }
//
//        Long minFeedId = getOldestFeed(uid);
//        // 都比用户朋友圈里最老的feed都老了，无需同步了，认为是已经被清理的了
//        if(friendFeeds.get(0) <= minFeedId) {
//            LOGGER.info("act=copyFriendFeedToMe latestFeedId <= minFeedId uid={} friendUid={} minFeedId={}", uid, friendUid, minFeedId);
//            return;
//        }
//
//        List<FeedSourceModel> feeds = contentService.batchGetFeeds(friendFeeds);
//        if(CollectionUtils.isEmpty(feeds)) {
//            LOGGER.error("act=copyFriendFeedToMe fail feeds isEmpty uid={} friendUid={} minFeedId={}",
//                    uid, friendUid, minFeedId, FeedLogTypeEnum.BUSINESS_HIGH, "MomentDataLogic");
//            return;
//        }
//
//        // 通过 friendMaxFeedId 来判定是否同步过，可能会少同步feed的情况
//        // 例如：当feedID为10和feedID为9的同时同步用户A的收件箱，当feedID为10的先同步进去了，feedID为9的同步时发现用户A正好不是活跃用户了，所以feedID为9没有同步进去
//        Long friendMaxFeedId = getSenderMaxFeedId(uid, friendUid);
//        for(FeedSourceModel feed : feeds) {
//            if(friendMaxFeedId == null || feed.getFeedId() > friendMaxFeedId) {
//                MomentRecordDO momentRecord = new MomentRecordDO();
//                momentRecord.setUid(uid);
//                momentRecord.setFeedId(feed.getFeedId());
//                momentRecord.setFeedOwnerId(feed.getUid());
//                momentRecord.setFeedCreateTime(feed.getFeedCreateTime());
//                this.saveMomentRecord(momentRecord);
//            }
//        }

        // 数据一致校验和处理
        {
            executorService.submit(() -> {
//                try {
//                    boolean isFriend = userFriendFacade.isFriend(uid, friendUid);
//                    if(isFriend) {
//                        return;
//                    }
//
//                    this.delFriendFeedFromMe(uid, friendUid);
//
//                    LOGGER.info("act=dataConsistencyCheck delFriendFeedFromMe msg=同步好友feed发生数据不一致 uid={} friendUid={}", uid, friendUid);
//                }
//                catch (Exception e) {
//                }
            });
        }
    }

    default String getMomentCacheKey(String uid) {
        if(StringUtils.isEmpty(uid)) {
            throw new FeedException();
        }

        return ACTIVE_MOMENTS + uid;
    }
}
