package com.github.content.feed.app;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.CreateCheckerManager;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.model.create.CreateFeedResult;
import com.github.content.feed.app.processor.CreateProcessorManager;
import com.github.content.feed.domain.FeedDomainService;
import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedStateEnum;
import com.github.content.feed.domain.model.FeedModel;
import com.github.content.feed.exception.FeedException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * feed业务服务.
 *
 * @author yanghuan
 */
@Service
public class FeedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedService.class);

    @Autowired
    private CreateCheckerManager createProcessorChecker;
    @Autowired
    private CreateProcessorManager createProcessorManager;
    @Autowired
    private FeedDomainService feedDomainService;

    /**
     * 创建feed 核心流程如下：
     * 校验 -> 创建feed前置处理 -> 创建feed -> feed后置处理 -> 生命周期变化通知.
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

        // processor beforeCreateFeed
        createProcessorManager.beforeCreateFeed(feedCreateBO);

        // to create feed
        long feedId = feedDomainService.createFeed(feedCreateBO);

        // processor afterCreateFeed
        createProcessorManager.afterCreateFeed(feedId, feedCreateBO);

        result.setSucceed(true);
        result.setFeedId(feedId);
        return result;
    }

    /**
     * 用户删除feed.
     */
    public void deleteFeed(String uid, Long feedId) {
        if(StringUtils.isEmpty(uid) || feedId == null) {
            throw new FeedException();
        }

        FeedModel feedModel = feedDomainService.getFeed(feedId);
        if(feedModel == null) {
            throw new FeedException();
        }

        if(!StringUtils.equals(uid, feedModel.getFeedUid())) {
            throw new FeedException();
        }

        feedDomainService.deleteFeed(feedId);
    }

    /**
     * 修改feed的可见范围.
     */
    public void changeFeedPrivilege(String uid, Long feedId, FeedPrivilegeEnum targetPrivilege) {
        if(StringUtils.isEmpty(uid) || feedId == null || targetPrivilege == null) {
            throw new FeedException();
        }

        FeedModel feedModel = feedDomainService.getFeed(feedId);
        if(feedModel == null) {
            throw new FeedException();
        }

        if(!StringUtils.equals(uid, feedModel.getFeedUid())) {
            throw new FeedException();
        }

        feedDomainService.changeFeedPrivilege(feedId, targetPrivilege);
    }

    /**
     * 管理员-后台-隐藏feed.
     */
    public void hideFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        feedDomainService.hideFeed(feedId);
    }

    /**
     * 管理员-后台-恢复feed.
     */
    public void openFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        feedDomainService.openFeed(feedId);
    }

    /**
     * 管理员-后台-将删除的feed -> 恢复正常
     */
    public void reviveFeed(String operator, Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        FeedModel feedModel = feedDomainService.getFeed(feedId);
        if(feedModel == null) {
            throw new FeedException();
        }

        feedDomainService.reviveFeed(feedId);
    }

    /**
     * 修改feed话题信息.
     */
    public void updateFeedTags(Long feedId, List<Long> newTagIdList) {
        if(feedId == null) {
            throw new FeedException();
        }

        FeedModel feedModel = feedDomainService.getFeed(feedId);
        if(feedModel == null) {
            throw new FeedException();
        }

        List<Long> oldTagIdList = feedModel.getTagIds();

        newTagIdList = newTagIdList == null ? new ArrayList<>() : newTagIdList;
        oldTagIdList = oldTagIdList == null ? new ArrayList<>() : oldTagIdList;
        if(CollectionUtils.isEqualCollection(newTagIdList, oldTagIdList)) {
            return;
        }

        feedDomainService.updateFeedTags(feedId, newTagIdList);
    }

    /**
     * 获取feed信息，从这出去的不能有被删除的feed.
     * 注：被删除的feed只能feed领域内才可以访问，外部业务均不许访问，也绝对不可以对外开放出去否则以后会存在很大问题.
     */
    public FeedModel getFeed(Long feedId) {
        if(feedId == null) {
            throw new FeedException();
        }

        FeedModel feedModel = feedDomainService.getFeed(feedId);
        if(feedModel == null || feedModel.getState() == FeedStateEnum.DEL) {
            return null;
        }

        return feedModel;
    }

    /**
     * 批量获取feed信息，从这出去的不能有被删除的feed.
     * 注：被删除的feed只能feed领域内才可以访问，外部业务均不许访问，也绝对不可以对外开放出去否则以后会存在很大问题.
     */
    public List<FeedModel> batchGetFeeds(List<Long> feedIds) {
        if(CollectionUtils.isEmpty(feedIds)) {
            throw new FeedException();
        }

        List<FeedModel> feedModels = feedDomainService.batchGetFeeds(feedIds);
        if(CollectionUtils.isEmpty(feedModels)) {
            return Collections.emptyList();
        }

        return feedModels.stream().map(feedModel ->
                feedModel == null || feedModel.getState() == FeedStateEnum.DEL ? null : feedModel).collect(Collectors.toList());
    }
}
