package com.github.content.feed.app.checker.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanghuan
 * @date: 2021-05-27
 * @desc: 图片检查
 */
@Service
public class ImageTypeChecker implements Checker {

    @Autowired
    private FeedTrackFacade feedTrackFacade;

    private static final int COUNT = 9;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        MultiImageFeedCreateBO multiImageFeedCreate = (MultiImageFeedCreateBO) feedCreateModel;
        FeedGradeConfig feedGradeConfig = ApolloConfigUtil.getFeedGradeConfig();
        List<String> whiteUserIdList = feedGradeConfig.getWhiteUserId();
        if(feedGradeConfig.getCanPublishImage() == 0) {
            // 获取限制的白名单，没有的话 全量关  有的话打开
            if(CollectionUtils.isEmpty(whiteUserIdList) || whiteUserIdList.contains(feedCreateModel.getUid())) {
                feedTrackFacade.trackPublishFeedFail(feedCreateModel, "maintaining");
                return CheckResult.fail("平台维护中，暂时无法发布图片");
            }
        }

        List<String> urlList = multiImageFeedCreate.getImageUrlList();
        // 多图校验
        if(CollectionUtils.isEmpty(urlList) || urlList.size() > COUNT) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "picture_limit");
            return CheckResult.fail("图片feed图片数量等于0或者大于9");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.IMAGE_MULTI;
    }

    @Override
    public int order() {
        return 1;
    }
}
