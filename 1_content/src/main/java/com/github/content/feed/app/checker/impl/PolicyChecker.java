package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 政策相关校验.
 *
 * @author yanbghuan
 */
@Service
public class PolicyChecker implements Checker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyChecker.class);

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return null;
    }

    @Override
    public int order() {
        return 2;
    }
}
