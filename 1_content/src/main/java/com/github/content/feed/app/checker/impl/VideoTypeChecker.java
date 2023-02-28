package com.github.content.feed.app.checker.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 视频检查
 * @author yanghuan
 */
@Service
public class VideoTypeChecker implements Checker {

    @Autowired
    private FeedTrackFacade feedTrackFacade;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        VideoFeedCreateBO videoFeedCreateBO = (VideoFeedCreateBO) feedCreateModel;
        if(StringUtils.isEmpty(videoFeedCreateBO.getVideoUrl())) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "video_null");
            return CheckResult.fail("视频数据为空，请重新编辑后发送");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.VIDEO_PLAY_NEW;
    }

    @Override
    public int order() {
        return 1;
    }
}
