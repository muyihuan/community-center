package com.github.content.feed.app.checker.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 参数合法性校验
 * @author yanghuan
 */
@Service
public class ParamLegalChecker implements Checker {

    @Autowired
    private FeedTrackFacade feedTrackFacade;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        if(feedCreateModel == null) {
            return CheckResult.fail("发布参数为空");
        }

        if(StringUtils.isEmpty(feedCreateModel.getUid())) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "other");
            return CheckResult.fail("用户id为空");
        }

        if(feedCreateModel.getContentType() == null) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "other");
            return CheckResult.fail("没有指定发布的类型");
        }

        if(feedCreateModel.getPrivilege() == null) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "other");
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
