package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.model.create.ForwardFeedCreateBO;
import com.github.content.feed.domain.FeedDomainService;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import com.github.content.feed.domain.enums.FeedStateEnum;
import com.github.content.feed.domain.model.FeedModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 转发类型feed特有校验.
 *
 * @author yanghuan
 */
@Service
public class ForwardFeedChecker implements Checker {

    @Autowired
    private FeedDomainService feedDomainService;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        ForwardFeedCreateBO forwardFeedCreateBO = (ForwardFeedCreateBO) feedCreateBO;
        if(StringUtils.isNotEmpty(feedCreateBO.getTextContent()) && feedCreateBO.getTextContent().length() > 200) {
            return CheckResult.fail("字数超过200字限制");
        }

        FeedModel forwardFeedSource = feedDomainService.getFeed(forwardFeedCreateBO.getForwardFeedId());
        if(forwardFeedSource == null || forwardFeedSource.getState() == FeedStateEnum.DEL) {
            return CheckResult.fail("被转发feed已删除");
        }

        FeedContentTypeEnum sourceFeedContentType = forwardFeedSource.getContentType();
        if (sourceFeedContentType == FeedContentTypeEnum.LINK) {
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
