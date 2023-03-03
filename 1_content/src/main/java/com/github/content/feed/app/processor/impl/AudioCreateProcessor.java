package com.github.content.feed.app.processor.impl;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.springframework.stereotype.Service;

/**
 * 音频feed创建特有处理.
 *
 * @author yanghuan
 */
@Service
public class AudioCreateProcessor extends AbstractEmptyCreateProcessor {

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.AUDIO;
    }

    @Override
    public int order() {
        return 0;
    }
}
