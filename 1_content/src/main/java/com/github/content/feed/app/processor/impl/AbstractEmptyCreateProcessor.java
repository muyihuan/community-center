package com.github.content.feed.app.processor.impl;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.app.processor.CreateProcessor;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;

/**
 * 空处理.
 *
 * @author yanghuan
 */
public abstract class AbstractEmptyCreateProcessor implements CreateProcessor {

    @Override
    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel) {

    }

    @Override
    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {

    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return null;
    }
}
