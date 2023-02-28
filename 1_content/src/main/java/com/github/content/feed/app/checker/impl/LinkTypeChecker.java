package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 链接feed检查
 * @createTime 2021年05月24日
 */
@Service
public class LinkTypeChecker implements Checker {

    @Autowired
    private FeedTrackFacade feedTrackFacade;

    /**
     * 链接feed的feed文字内容长度阈值
     */
    private static final int TEXT_LIMIT = 200;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        if(StringUtils.isNotEmpty(feedCreateModel.getTextContent()) && feedCreateModel.getTextContent().length() > TEXT_LIMIT) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "other");
            return CheckResult.fail("字数超过200字限制");
        }

        LinkFeedCreateBO linkFeedCreateBO = (LinkFeedCreateBO) feedCreateModel;
        if(StringUtils.isEmpty(linkFeedCreateBO.getLinkTitle()) &&
           StringUtils.isEmpty(linkFeedCreateBO.getLinkDesc()) &&
           StringUtils.isEmpty(linkFeedCreateBO.getLinkIcon()))
        {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "link_null");
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
