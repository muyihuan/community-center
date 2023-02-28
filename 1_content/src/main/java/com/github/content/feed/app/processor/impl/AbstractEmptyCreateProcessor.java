package com.github.content.feed.app.processor.impl;

import com.github.content.feed.app.processor.CreateProcessor;

/**
 *
 * @author yanghuan
 */
public abstract class AbstractEmptyCreateProcessor implements CreateProcessor {

    @Override
    public void beforeCreateUgc(AbstractFeedCreateBO feedCreateModel) {

    }

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
