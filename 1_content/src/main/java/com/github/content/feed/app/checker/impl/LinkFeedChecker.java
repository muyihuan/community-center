package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.model.create.LinkFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 链接类型feed特有校验.
 *
 * @author yanghuan
 */
@Service
public class LinkFeedChecker implements Checker {

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        if(StringUtils.isNotEmpty(feedCreateBO.getTextContent()) && feedCreateBO.getTextContent().length() > 200) {
            return CheckResult.fail("字数超过200字限制");
        }

        LinkFeedCreateBO linkFeedCreateBO = (LinkFeedCreateBO) feedCreateBO;
        if(StringUtils.isEmpty(linkFeedCreateBO.getLinkTitle()) &&
           StringUtils.isEmpty(linkFeedCreateBO.getLinkDesc()) &&
           StringUtils.isEmpty(linkFeedCreateBO.getLinkIcon()))
        {
            return CheckResult.fail("链接信息为空");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.LINK;
    }

    @Override
    public int order() {
        return 1;
    }
}
