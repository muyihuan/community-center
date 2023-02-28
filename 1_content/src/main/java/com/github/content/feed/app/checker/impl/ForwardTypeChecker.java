package com.github.content.feed.app.checker.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 转发feed检查
 * @author yanghuan
 */
@Service
public class ForwardTypeChecker implements Checker {

    @Autowired
    private FeedDomainService feedService;

    @Autowired
    private FeedTrackFacade feedTrackFacade;

    /**
     * 文字内容长度阈值
     */
    private static final int TEXT_LIMIT = 200;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        FeedGradeConfig feedGradeConfig = ApolloConfigUtil.getFeedGradeConfig();
        List<String> whiteUserIdList = feedGradeConfig.getWhiteUserId();
        if (feedGradeConfig.getCanCommentAndForward() == 0) {
            if (CollectionUtils.isEmpty(whiteUserIdList) || whiteUserIdList.contains(feedCreateModel.getUid())) {
                feedTrackFacade.trackPublishFeedFail(feedCreateModel, "maintaining");
                return CheckResult.fail("服务降级，暂不支持转发");
            }
        }

        ForwardFeedCreateBO forwardFeedCreateBO = (ForwardFeedCreateBO) feedCreateModel;
        if(StringUtils.isNotEmpty(feedCreateModel.getTextContent()) && feedCreateModel.getTextContent().length() > TEXT_LIMIT) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "other");
            return CheckResult.fail("字数超过200字限制");
        }

        FeedSourceModel forwardFeedSource = feedService.getFeed(forwardFeedCreateBO.getForwardFeedId());
        if(forwardFeedSource == null || forwardFeedSource.getState() == FeedStateEnum.DEL) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "other");
            return CheckResult.fail("被转发feed已删除");
        }

        FeedSourceTypeEnum forwardSourceType = forwardFeedSource.getSourceType();
        if (forwardSourceType == FeedSourceTypeEnum.CHAMPION || forwardSourceType == FeedSourceTypeEnum.GAMBLING_KING ||
            forwardSourceType == FeedSourceTypeEnum.MEDAL || forwardSourceType == FeedSourceTypeEnum.RICH_NEW ||
            forwardSourceType == FeedSourceTypeEnum.RICH)
        {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "other");
            return CheckResult.fail("该feed类型不支持转发");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.FORWARD;
    }

    @Override
    public int order() {
        return 1;
    }
}
