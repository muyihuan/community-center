package com.github.content.feed.app;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.CreateProcessorChecker;
import com.github.content.feed.app.model.CreateFeedResult;
import com.github.content.feed.app.processor.CreateProcessorManager;
import com.github.content.feed.domain.FeedDomainService;
import com.github.content.feed.exception.FeedException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容相关服务.
 *
 * @author yanghuan
 */
@Service
public class FeedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedService.class);

    @Autowired
    private CreateProcessorChecker createProcessorChecker;
    @Autowired
    private CreateProcessorManager createProcessorManager;
    @Autowired
    private UgcService ugcService;
    @Autowired
    private FeedDomainService feedService;

    /**
     * 创建feed 核心流程如下：
     * 校验 -> 创建ugc前置处理 -> 创建ugc -> 创建feed前置处理 -> 创建feed -> feed后置处理 -> 生命周期通知
     */
    public CreateFeedResult createFeed(AbstractFeedCreateBO feedCreateBO) {
        CreateFeedResult result = new CreateFeedResult();

        if (feedCreateBO == null) {
            result.setSucceed(false);
            result.setFailMsg("参数为空");
            return result;
        }

        // create feed param check
        CheckResult check = createProcessorChecker.check(feedCreateBO);
        if (check == null || !check.getSuccess()) {
            result.setSucceed(false);
            result.setFailMsg(check == null ? "" : check.getResultMsg());
            return result;
        }

        // processor beforeCreateUgc
        createProcessorManager.beforeCreateUgc(feedCreateBO);

        // need create sourceId
        if (feedCreateBO.getSourceId() == null) {
            // create ugc, prepare sourceType and sourceId
            ugcService.createUgc(feedCreateBO);
        }

        // check sourceId and sourceType
        if (feedCreateBO.getSourceId() == null || feedCreateBO.getSourceId() <= 0 || feedCreateBO.getSourceType() == null) {
            result.setSucceed(false);
            result.setFailMsg("sourceId 有误");
            return result;
        }

        // processor beforeCreateFeed
        createProcessorManager.beforeCreateFeed(feedCreateBO);

        // to create feed
        long feedId = feedService.createFeed(feedCreateBO);

        // processor afterCreateFeed
        createProcessorManager.afterCreateFeed(feedId, feedCreateBO);

        result.setSucceed(true);
        result.setFeedId(feedId);
        return result;
    }

    /**
     * 用户删除feed
     */
    public void deleteFeed(String uid, Long feedId) {
        if(StringUtils.isEmpty(uid) || feedId == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null) {
            throw new FeedException(FeedContentError.FEED_NOT_FOUND);
        }

        if(!StringUtils.equals(uid, feedSource.getUid())) {
            throw new FeedException(FeedContentError.OPT_ILLEGAL_ERROR);
        }

        feedService.deleteFeed(feedId);
        feedSource.setState(FeedStateEnum.DEL);

        // feed的ugc一旦删除，恢复就不太可能了
        deleteFeedUgc(feedSource);
    }

    /**
     * 修改feed的可见范围
     */
    public void changeFeedPrivilege(String uid, Long feedId, FeedPrivilegeEnum targetPrivilege) {
        if(StringUtils.isEmpty(uid) || feedId == null || targetPrivilege == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null) {
            throw new FeedException(FeedContentError.FEED_NOT_FOUND);
        }

        if(!StringUtils.equals(uid, feedSource.getUid())) {
            throw new FeedException(FeedContentError.OPT_ILLEGAL_ERROR);
        }

        feedService.changeFeedPrivilege(feedId, targetPrivilege);
    }

    /**
     * 系统删除feed
     */
    public void deleteFeedBySystem(String operator, Long feedId) {
        if(StringUtils.isEmpty(operator) || feedId == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null) {
            throw new FeedException(FeedContentError.FEED_NOT_FOUND);
        }

        feedService.deleteFeed(feedId);
        feedSource.setState(FeedStateEnum.DEL);

        // feed的ugc一旦删除，恢复就不太可能了
        deleteFeedUgc(feedSource);
    }

    /**
     * 管理员-后台-隐藏feed
     */
    public void hideFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        feedService.hideFeed(feedId);
    }

    /**
     * 管理员-后台-恢复feed
     */
    public void openFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        feedService.openFeed(feedId);
    }

    /**
     * 管理员-后台-将删除的feed -> 恢复正常
     */
    public void reviveFeed(String operator, Long feedId) {
        if(feedId == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null) {
            throw new FeedException(FeedContentError.FEED_NOT_FOUND);
        }

        feedService.reviveFeed(feedId);
        feedSource.setState(FeedStateEnum.NORMAL);
    }

    /**
     * 修改feed话题信息
     */
    public void updateFeedTags(Long feedId, List<Long> newTagIdList) {
        if(feedId == null) {
            throw new FeedException(FeedContentError.FEED_PARAM_ERROR);
        }

        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null) {
            throw new FeedException(FeedContentError.FEED_NOT_FOUND);
        }

        List<Long> oldTagIdList = feedSource.getTagIds();

        newTagIdList = newTagIdList == null ? new ArrayList<>() : newTagIdList;
        oldTagIdList = oldTagIdList == null ? new ArrayList<>() : oldTagIdList;
        if(CollectionUtils.isEqualCollection(newTagIdList, oldTagIdList)) {
            return;
        }

        feedService.updateFeedTags(feedId, newTagIdList);
    }

    /**
     * 获取feed信息，从这出去的不能有被删除的feed
     * 注：被删除的feed只能feed领域内才可以访问，外部业务均不许访问，也绝对不可以对外开放出去否则以后会存在很大问题
     */
    public FeedSourceModel getFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException(FeedContentError.FEED_QUERY_ERROR);
        }

        FeedSourceModel feedSource = feedService.getFeed(feedId);
        if(feedSource == null || feedSource.getState() == FeedStateEnum.DEL) {
            return null;
        }

        return feedSource;
    }

    /**
     * 批量获取feed信息，从这出去的不能有被删除的feed
     * 注：被删除的feed只能feed领域内才可以访问，外部业务均不许访问，也绝对不可以对外开放出去否则以后会存在很大问题
     */
    public List<FeedSourceModel> batchGetFeeds(List<Long> feedIds) {
        if(CollectionUtils.isEmpty(feedIds)) {
            throw new FeedException(FeedContentError.FEED_QUERY_ERROR);
        }

        List<FeedSourceModel> feedSources = feedService.batchGetFeeds(feedIds);
        if(CollectionUtils.isEmpty(feedSources)) {
            return Collections.emptyList();
        }

        return feedSources.stream().map(feedSource ->
               feedSource == null || feedSource.getState() == FeedStateEnum.DEL ? null : feedSource).collect(Collectors.toList());
    }

    /**
     * feed的ugc一旦删除，恢复就不太可能了，所以只有用户删除的才会删除ugc内容，后台删除不会删除ugc内容
     */
    private void deleteFeedUgc(FeedSourceModel feedSource) {

    }
}
