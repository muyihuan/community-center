package com.github.content.feed.app.checker.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 文字内容检查
 * @createTime 2021年05月12日 11:17:00
 */
@Service
public class TextTypeChecker implements Checker {

    @Autowired
    private FeedTrackFacade feedTrackFacade;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        if (StringUtils.isBlank(feedCreateModel.getTextContent())) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "content_null");
            return CheckResult.fail("内容不能为空");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.TEXT;
    }

    @Override
    public int order() {
        return 1;
    }
}
