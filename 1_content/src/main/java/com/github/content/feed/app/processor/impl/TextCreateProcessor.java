package com.github.content.feed.app.processor.impl;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.springframework.stereotype.Service;

/**
 * 文字feed创建特有处理.
 *
 * @author yanghuan
 */
@Service
public class TextCreateProcessor extends AbstractEmptyCreateProcessor {

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.TEXT;
    }

    @Override
    public int order() {
        return 0;
    }
}
