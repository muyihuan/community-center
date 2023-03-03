package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 参数合法性校验.
 *
 * @author yanghuan
 */
@Service
public class ParamLegalChecker implements Checker {

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        if(feedCreateBO == null) {
            return CheckResult.fail("发布参数为空");
        }

        if(StringUtils.isEmpty(feedCreateBO.getUid())) {
            return CheckResult.fail("用户id为空");
        }

        if(feedCreateBO.getContentType() == null) {
            return CheckResult.fail("没有指定发布的类型");
        }

        if(feedCreateBO.getPrivilege() == null) {
            return CheckResult.fail("没有指定可见范围");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return null;
    }

    @Override
    public int order() {
        return 0;
    }
}
