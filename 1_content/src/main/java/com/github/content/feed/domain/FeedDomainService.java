package com.github.content.feed.domain;

import com.github.content.feed.domain.model.lifecycle.FeedLifeCycleProtocol;
import com.github.content.feed.infra.MqProducer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * feed从出生到死亡的生命周期.
 *
 * @author yanghuan
 */
@Service
public class FeedDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedDomainService.class);

    @Autowired
    private MqProducer mqProducer;

    private static final String KEY_FEED_LIFECYCLE_CHANGE = "feed:life:change:";

    private static final String FEED_LIFECYCLE_CHANGE_TOPIC = "feed_life_cycle_change";

    /**
     * 创建feed，将feed的各个信息组成一个feed并存储
     */
    public long createFeed(AbstractFeedCreateBO feedCreateModel) {
        long feedId = this.generateFeedId();

        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            feedSourceLogic.createFeed(feedSource);

            FeedSourceModel feedSourceModel = getFeed(feedId);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedCreateModel.getContentType(),
                    feedCreateModel.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(FeedPrivilegeEnum.getByCode(feedCreateModel.getPrivilege()), feedCreateModel.getSystemPrivilege()),
                    new FeedBaseInfo(feedSourceModel.getFeedCreateTime(), feedSourceModel.getSourceType(), feedSourceModel.getSourceId(),
                            feedSourceModel.getTagIds(), feedSourceModel.getAtInfoList(), feedSourceModel.getCardInfo(), feedSourceModel.getLabelInfo())
            )));

            LOGGER.info("act=createFeed end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }

        return feedId;
    }

    /**
     * 删除feed，删除feed，feed不再可用
     */
    public void deleteFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            feedSourceLogic.updateFeedState(feedId, FeedStateEnum.DEL);

            // 广播feed变化消息
            FeedSourceModel feedSource = getFeed(feedId);
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.DEL,
                    parseFeedPrivilege(feedSource.getPrivilege(), feedSource.getSystemPrivilege()),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo())
            )));

            // 上报dataHub
            dataHubFacade.produce(FEED_LIFECYCLE_CHANGE_TOPIC, new DataHupFeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType().getType(),
                    feedSource.getUid(),
                    DataHupFeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE.getCode(),
                    FeedLifeCycleProtocol.FeedState.DEL.getCode(),
                    parseFeedPrivilege(feedSource.getPrivilege(), feedSource.getSystemPrivilege()).getCode(),
                    WbJSON.toJson(new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo()))
            ));

            LOGGER.info("act=deleteFeed end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 修改feed的可见范围
     * 注：目前只支持修改为仅自己可见，其他的需要适配一下
     */
    public void changeFeedPrivilege(Long feedId, FeedPrivilegeEnum targetPrivilege) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            if(targetPrivilege != FeedPrivilegeEnum.SELF_VIEW) {
                throw new FeedException(FeedContentError.OPT_NOT_SUPPORT_ERROR);
            }

            // 无需修改
            if(feedSource.getPrivilege() == targetPrivilege) {
                return;
            }

            feedSourceLogic.updateFeedPrivilege(feedId, targetPrivilege);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(targetPrivilege, feedSource.getSystemPrivilege()),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo())
            )));

            LOGGER.info("act=changeFeedPrivilege end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 隐藏feed，和删除feed不同，feed的隐藏只是不展示其他用户，feed作者仍可见
     */
    public void hideFeed(Long feedId) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            if(feedSource.getSystemPrivilege() != FeedSystemPrivilegeEnum.DEF) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            feedSourceLogic.updateFeedState(feedId, FeedStateEnum.SELF_VIEW);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(feedSource.getPrivilege(), FeedSystemPrivilegeEnum.FEED_SELF_VIEW),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo())
            )));

            LOGGER.info("act=hideFeed end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 开放feed，恢复原本可见性
     */
    public void openFeed(Long feedId) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            if(feedSource.getSystemPrivilege() != FeedSystemPrivilegeEnum.FEED_SELF_VIEW) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            feedSourceLogic.updateFeedState(feedId, FeedStateEnum.NORMAL);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC,WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(feedSource.getPrivilege(), FeedSystemPrivilegeEnum.DEF),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo())
            )));

            LOGGER.info("act=openFeed end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 隐藏feed ugc内容，和隐藏feed不同，隐藏ugc内容只是代表ugc内容不可以让别人看到，展示feed的时候需要做一些脱敏的处理
     */
    public void hideFeedUgc(Long feedId) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            if(feedSource.getSystemPrivilege() != FeedSystemPrivilegeEnum.DEF) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            feedSourceLogic.updateFeedState(feedId, FeedStateEnum.IN_AUDIT);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(feedSource.getPrivilege(), FeedSystemPrivilegeEnum.UGC_SELF_VIEW),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo())
            )));

            LOGGER.info("act=hideFeedUgc end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 开放feed ugc内容，feed ugc内容允许他人查看
     */
    public void openFeedUgc(Long feedId) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            if(feedSource.getSystemPrivilege() != FeedSystemPrivilegeEnum.UGC_SELF_VIEW) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            feedSourceLogic.updateFeedState(feedId, FeedStateEnum.NORMAL);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(feedSource.getPrivilege(), FeedSystemPrivilegeEnum.DEF),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo())
            )));

            LOGGER.info("act=openFeedUgc end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 复活feed，被删除的feed重新可用
     */
    public void reviveFeed(Long feedId) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() != FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            feedSourceLogic.updateFeedState(feedId, FeedStateEnum.NORMAL);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.VISIBILITY_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(feedSource.getPrivilege(), FeedSystemPrivilegeEnum.DEF),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            feedSource.getTagIds(), feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo())
            )));

            LOGGER.info("act=reviveFeed end feedId={}", feedId);
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 修改feed话题信息
     */
    public void updateFeedTags(Long feedId, List<Long> tagIdList) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            String tagsStr = CollectionUtils.isNotEmpty(tagIdList) ? Joiner.on(",").join(tagIdList) : "";
            feedSourceLogic.updateFeedTags(feedId, tagsStr);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, WbJSON.toJson(new FeedLifeCycleProtocol(
                    feedId,
                    feedSource.getContentType(),
                    feedSource.getUid(),
                    FeedLifeCycleProtocol.ChangeType.CONTENT_CHANGE,
                    FeedLifeCycleProtocol.FeedState.NORMAL,
                    parseFeedPrivilege(feedSource.getPrivilege(), feedSource.getSystemPrivilege()),
                    new FeedBaseInfo(feedSource.getFeedCreateTime(), feedSource.getSourceType(), feedSource.getSourceId(),
                            tagIdList, feedSource.getAtInfoList(), feedSource.getCardInfo(), feedSource.getLabelInfo()),
                    new FeedModifyInfo(feedSource.getTagIds()),
                    new FeedModifyInfo(tagIdList))));

            LOGGER.info("act=updateFeedTags end feedId={} tagIdList={}", feedId, WbJSON.toJson(tagIdList));
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 修改feed话题信息
     */
    public void updateFeedScene(Long feedId, SceneInfoBO sceneInfo) {
        RLock lock = dcsLock.tryLock(KEY_FEED_LIFECYCLE_CHANGE + feedId, 200, -1, TimeUnit.MILLISECONDS);
        try {
            if (lock == null) {
                throw new FeedException(FeedContentError.OPERATE_FREQUENTLY_ERROR);
            }

            FeedSourceModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException(FeedContentError.FEED_NOT_FOUND);
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException(FeedContentError.UPDATE_FEED_ERROR);
            }

            if(sceneInfo == null) {
                feedSourceLogic.updateFeedScene(feedId, null, null);
            }
            else {
                feedSourceLogic.updateFeedScene(feedId, sceneInfo.getScene() == null ? null : sceneInfo.getScene().getValue(), sceneInfo.getSceneParam());
            }

            LOGGER.info("act=updateFeedScene end feedId={} sceneInfo={}", feedId, sceneInfo == null ? "null" : WbJSON.toJson(sceneInfo));
        }
        finally {
            dcsLock.unLock(lock);
        }
    }

    /**
     * 获取feed信息
     */
    public FeedSourceModel getFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException(FeedContentError.FEED_QUERY_ERROR);
        }

        FeedSource feedSourceDO = feedSourceLogic.getFeedById(feedId);
        if(feedSourceDO == null) {
            return null;
        }

        return contentParser.parseFeedContent(feedSourceDO);
    }

    /**
     * 批量获取feed信息
     */
    public List<FeedSourceModel> batchGetFeeds(List<Long> feedIds) {
        if(CollectionUtils.isEmpty(feedIds)) {
            throw new FeedException(FeedContentError.FEED_QUERY_ERROR);
        }

        List<FeedSource> feedSourceList = feedSourceLogic.batchGetFeedByIds(feedIds);
        if(CollectionUtils.isEmpty(feedSourceList)) {
            return Collections.emptyList();
        }

        return feedSourceList.stream().map(feed -> {
            try {
                return contentParser.parseFeedContent(feed);
            }
            catch (Exception e) {
                LOGGER.info("act=parseFeedContent feedIds={}", WbJSON.toJson(feedIds));
            }

            return null;
        }).collect(Collectors.toList());
    }

    /**
     * feed可见性解析
     */
    private FeedLifeCycleProtocol.FeedPrivilege parseFeedPrivilege(FeedPrivilegeEnum feedPrivilege, FeedSystemPrivilegeEnum systemPrivilege) {
        if(systemPrivilege != null) {
            // 现在用户侧可以设置的可见范围没有自见，所以不用考虑两个可见性范围冲突问题
            if(systemPrivilege == FeedSystemPrivilegeEnum.UGC_SELF_VIEW) {
                return FeedLifeCycleProtocol.FeedPrivilege.SELF_VIEW;
            }
            else if(systemPrivilege == FeedSystemPrivilegeEnum.FEED_SELF_VIEW) {
                return FeedLifeCycleProtocol.FeedPrivilege.SELF_VIEW;
            }
        }

        if(feedPrivilege == FeedPrivilegeEnum.FRIEND_VIEW) {
            return FeedLifeCycleProtocol.FeedPrivilege.FRIEND_VIEW;
        }
        else if(feedPrivilege == FeedPrivilegeEnum.SQUARE_VIEW) {
            return FeedLifeCycleProtocol.FeedPrivilege.ALL_VIEW;
        }
        else if(feedPrivilege == FeedPrivilegeEnum.STRANGER_VIEW) {
            return FeedLifeCycleProtocol.FeedPrivilege.STRANGER_VIEW;
        }
        else if(feedPrivilege == FeedPrivilegeEnum.SELF_VIEW) {
            return FeedLifeCycleProtocol.FeedPrivilege.SELF_VIEW;
        }

        return FeedLifeCycleProtocol.FeedPrivilege.ALL_VIEW;
    }

    /**
     * 生成feedId
     */
    private long generateFeedId() {
        return 0L;
    }
}
