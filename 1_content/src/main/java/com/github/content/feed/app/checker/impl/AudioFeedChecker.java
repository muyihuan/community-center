package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.model.create.AudioFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 音频类型feed特有校验.
 *
 * @author yanghuan
 */
@Service
public class AudioFeedChecker implements Checker {

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        // 降级开关

        AudioFeedCreateBO audioFeedCreateBO = (AudioFeedCreateBO) feedCreateBO;
        if(StringUtils.isEmpty(audioFeedCreateBO.getAudioUrl())) {
            return CheckResult.fail("语音数据为空，请重新编辑后发送");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.AUDIO;
    }

    @Override
    public int order() {
        return 1;
    }
}
