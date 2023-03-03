package com.github.content.feed.domain;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedStateEnum;
import com.github.content.feed.domain.enums.FeedSystemPrivilegeEnum;
import com.github.content.feed.domain.model.FeedModel;
import com.github.content.feed.domain.model.lifecycle.FeedLifeCycleEvent;
import com.github.content.feed.domain.repo.FeedRepository;
import com.github.content.feed.domain.repo.model.FeedDO;
import com.github.content.feed.exception.FeedException;
import com.github.content.feed.infra.MqProducer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * feed领域能力.
 *
 * @author yanghuan
 */
@Service
public class FeedDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedDomainService.class);

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private MqProducer mqProducer;

    private static final String KEY_FEED_LIFECYCLE_CHANGE = "feed:life:change:";

    private static final String FEED_LIFECYCLE_CHANGE_TOPIC = "feed_life_cycle_change";

    /**
     * 创建feed，将feed的各个信息组成一个feed并存储.
     */
    public long createFeed(AbstractFeedCreateBO feedCreateModel) {
        long feedId = this.generateFeedId();

        // lock.
        try {
            feedRepository.createFeed(null);

            FeedModel feedSourceModel = getFeed(feedId);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, toJson(new FeedLifeCycleEvent(
                    feedId,
                    feedCreateModel.getContentType().getType(),
                    feedCreateModel.getUid(),
                    FeedLifeCycleEvent.ChangeType.BORN.getCode(),
                    FeedLifeCycleEvent.FeedState.NORMAL.getCode(),
                    FeedLifeCycleEvent.parseFeedPrivilege(feedCreateModel.getPrivilege(), feedCreateModel.getSystemPrivilege()).getCode(),
                    new FeedLifeCycleEvent.FeedBaseInfo(feedSourceModel.getAtInfoList(), feedSourceModel.getCreateTime(),
                            feedSourceModel.getTagIds())
            )));

            LOGGER.info("act=createFeed end feedId={}", feedId);
        }
        finally {
            // unlock.
        }

        return feedId;
    }

    /**
     * 删除feed，删除feed，feed不再可用.
     */
    public void deleteFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        // lock.
        try {
            feedRepository.updateFeedState(feedId, FeedStateEnum.DEL);

            // 广播feed变化消息
            FeedModel feedModel = getFeed(feedId);
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, toJson(new FeedLifeCycleEvent(
                    feedId,
                    feedModel.getContentType().getType(),
                    feedModel.getFeedUid(),
                    FeedLifeCycleEvent.ChangeType.VISIBILITY_CHANGE.getCode(),
                    FeedLifeCycleEvent.FeedState.DEL.getCode(),
                    FeedLifeCycleEvent.parseFeedPrivilege(feedModel.getPrivilege(), feedModel.getSystemPrivilege()).getCode(),
                    new FeedLifeCycleEvent.FeedBaseInfo(feedModel.getAtInfoList(), feedModel.getCreateTime(), feedModel.getTagIds())
            )));

            LOGGER.info("act=deleteFeed end feedId={}", feedId);
        }
        finally {
            // unlock.
        }
    }

    /**
     * 修改feed的可见范围.
     */
    public void changeFeedPrivilege(Long feedId, FeedPrivilegeEnum targetPrivilege) {
        // lock.
        try {
            FeedModel feedSource = getFeed(feedId);
            if(feedSource == null) {
                throw new FeedException();
            }

            if(feedSource.getState() == FeedStateEnum.DEL) {
                throw new FeedException();
            }

            // 无需修改
            if(feedSource.getPrivilege() == targetPrivilege) {
                return;
            }

            feedRepository.updateFeedPrivilege(feedId, targetPrivilege);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, toJson(new FeedLifeCycleEvent(
                    feedId,
                    feedSource.getContentType().getType(),
                    feedSource.getFeedUid(),
                    FeedLifeCycleEvent.ChangeType.VISIBILITY_CHANGE.getCode(),
                    FeedLifeCycleEvent.FeedState.NORMAL.getCode(),
                    FeedLifeCycleEvent.parseFeedPrivilege(targetPrivilege, feedSource.getSystemPrivilege()).getCode(),
                    new FeedLifeCycleEvent.FeedBaseInfo(feedSource.getAtInfoList(), feedSource.getCreateTime(),
                            feedSource.getTagIds())
            )));

            LOGGER.info("act=changeFeedPrivilege end feedId={}", feedId);
        }
        finally {
            // unlock.
        }
    }

    /**
     * 隐藏feed，和删除feed不同，feed的隐藏只是不展示其他用户，feed作者仍可见.
     */
    public void hideFeed(Long feedId) {
        // lock.
        try {
            FeedModel feedModel = getFeed(feedId);
            if(feedModel == null) {
                throw new FeedException();
            }

            if(feedModel.getState() == FeedStateEnum.DEL) {
                throw new FeedException();
            }

            if(feedModel.getSystemPrivilege() != FeedSystemPrivilegeEnum.DEF) {
                throw new FeedException();
            }

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, toJson(new FeedLifeCycleEvent(
                    feedId,
                    feedModel.getContentType().getType(),
                    feedModel.getFeedUid(),
                    FeedLifeCycleEvent.ChangeType.VISIBILITY_CHANGE.getCode(),
                    FeedLifeCycleEvent.FeedState.NORMAL.getCode(),
                    FeedLifeCycleEvent.parseFeedPrivilege(feedModel.getPrivilege(), FeedSystemPrivilegeEnum.FEED_SELF_VISIBLE).getCode(),
                    new FeedLifeCycleEvent.FeedBaseInfo(feedModel.getAtInfoList(), feedModel.getCreateTime(), feedModel.getTagIds())
            )));

            LOGGER.info("act=hideFeed end feedId={}", feedId);
        }
        finally {
            // unlock.
        }
    }

    /**
     * 开放feed，恢复原本可见性.
     */
    public void openFeed(Long feedId) {
        // lock.
        try {
            FeedModel feedModel = getFeed(feedId);
            if(feedModel == null) {
                throw new FeedException();
            }

            if(feedModel.getState() == FeedStateEnum.DEL) {
                throw new FeedException();
            }

            if(feedModel.getSystemPrivilege() != FeedSystemPrivilegeEnum.FEED_SELF_VISIBLE) {
                throw new FeedException();
            }

            feedRepository.updateFeedState(feedId, FeedStateEnum.NORMAL);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, toJson(new FeedLifeCycleEvent(
                    feedId,
                    feedModel.getContentType().getType(),
                    feedModel.getFeedUid(),
                    FeedLifeCycleEvent.ChangeType.VISIBILITY_CHANGE.getCode(),
                    FeedLifeCycleEvent.FeedState.NORMAL.getCode(),
                    FeedLifeCycleEvent.parseFeedPrivilege(feedModel.getPrivilege(), FeedSystemPrivilegeEnum.DEF).getCode(),
                    new FeedLifeCycleEvent.FeedBaseInfo(feedModel.getAtInfoList(), feedModel.getCreateTime(), feedModel.getTagIds())
            )));

            LOGGER.info("act=openFeed end feedId={}", feedId);
        }
        finally {
            // unlock.
        }
    }

    /**
     * 复活feed，被删除的feed重新可用.
     */
    public void reviveFeed(Long feedId) {
        // lock.
        try {
            FeedModel feedModel = getFeed(feedId);
            if(feedModel == null) {
                throw new FeedException();
            }

            if(feedModel.getState() != FeedStateEnum.DEL) {
                throw new FeedException();
            }

            feedRepository.updateFeedState(feedId, FeedStateEnum.NORMAL);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, toJson(new FeedLifeCycleEvent(
                    feedId,
                    feedModel.getContentType().getType(),
                    feedModel.getFeedUid(),
                    FeedLifeCycleEvent.ChangeType.VISIBILITY_CHANGE.getCode(),
                    FeedLifeCycleEvent.FeedState.NORMAL.getCode(),
                    FeedLifeCycleEvent.parseFeedPrivilege(feedModel.getPrivilege(), FeedSystemPrivilegeEnum.DEF).getCode(),
                    new FeedLifeCycleEvent.FeedBaseInfo(feedModel.getAtInfoList(), feedModel.getCreateTime(), feedModel.getTagIds())
            )));

            LOGGER.info("act=reviveFeed end feedId={}", feedId);
        }
        finally {
            // unlock.
        }
    }

    /**
     * 修改feed话题信息.
     */
    public void updateFeedTags(Long feedId, List<Long> tagIdList) {
        // lock.
        try {
            FeedModel feedModel = getFeed(feedId);
            if(feedModel == null) {
                throw new FeedException();
            }

            if(feedModel.getState() == FeedStateEnum.DEL) {
                throw new FeedException();
            }

            String tagsStr = "";
            feedRepository.updateFeedTags(feedId, tagsStr);

            // 广播feed变化消息
            mqProducer.sendMsg(FEED_LIFECYCLE_CHANGE_TOPIC, toJson(new FeedLifeCycleEvent(
                    feedId,
                    feedModel.getContentType().getType(),
                    feedModel.getFeedUid(),
                    FeedLifeCycleEvent.ChangeType.CONTENT_CHANGE.getCode(),
                    FeedLifeCycleEvent.FeedState.NORMAL.getCode(),
                    FeedLifeCycleEvent.parseFeedPrivilege(feedModel.getPrivilege(), feedModel.getSystemPrivilege()).getCode(),
                    new FeedLifeCycleEvent.FeedBaseInfo(feedModel.getAtInfoList(), feedModel.getCreateTime(), tagIdList),
                    new FeedLifeCycleEvent.FeedModifyInfo(feedModel.getTagIds()),
                    new FeedLifeCycleEvent.FeedModifyInfo(tagIdList))));

            LOGGER.info("act=updateFeedTags end feedId={} tagIdList={}", feedId, toJson(tagIdList));
        }
        finally {
            // unlock.
        }
    }

    /**
     * 获取feed信息.
     */
    public FeedModel getFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        FeedDO feedDO = feedRepository.getFeedById(feedId);
        if(feedDO == null) {
            return null;
        }

        return null;
    }

    /**
     * 批量获取feed信息.
     */
    public List<FeedModel> batchGetFeeds(List<Long> feedIds) {
        if(CollectionUtils.isEmpty(feedIds)) {
            throw new FeedException();
        }

        List<FeedDO> feedDOList = feedRepository.batchGetFeedByIds(feedIds);
        if(CollectionUtils.isEmpty(feedDOList)) {
            return Collections.emptyList();
        }

        return feedDOList.stream().map(feed -> {
            return new FeedModel();
        }).collect(Collectors.toList());
    }

    private String toJson(Object o) {
        return "";
    }

    /**
     * 生成feedId.
     */
    private long generateFeedId() {
        return 0L;
    }
}
