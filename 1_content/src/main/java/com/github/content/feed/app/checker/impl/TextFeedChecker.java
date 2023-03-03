package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 文字类型feed特有校验.
 *
 * @author yanghuan
 */
@Service
public class TextFeedChecker implements Checker {

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        if (StringUtils.isBlank(feedCreateBO.getTextContent())) {
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
