package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.model.create.VideoFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 视频类型feed特有校验.
 *
 * @author yanghuan
 */
@Service
public class VideoFeedChecker implements Checker {

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        VideoFeedCreateBO videoFeedCreateBO = (VideoFeedCreateBO) feedCreateBO;
        if(StringUtils.isEmpty(videoFeedCreateBO.getVideoUrl())) {
            return CheckResult.fail("视频数据为空，请重新编辑后发送");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.VIDEO;
    }

    @Override
    public int order() {
        return 1;
    }
}
