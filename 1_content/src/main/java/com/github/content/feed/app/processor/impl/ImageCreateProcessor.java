package com.github.content.feed.app.processor.impl;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import org.springframework.stereotype.Service;

/**
 * 图片feed创建特有处理.
 *
 * @author yanghuan
 */
@Service
public class ImageCreateProcessor extends AbstractEmptyCreateProcessor {

    @Override
    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {

    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.IMAGE;
    }

    @Override
    public int order() {
        return 0;
    }
}
