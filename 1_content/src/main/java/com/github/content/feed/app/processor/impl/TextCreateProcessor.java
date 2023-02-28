package com.github.content.feed.app.processor.impl;

import org.springframework.stereotype.Service;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 文字feed创建处理
 * @createTime 2021年07月05日 17:44:00
 */
@Service
public class TextCreateProcessor extends AbstractEmptyCreateProcessor {

    @Override
    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {

    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.TEXT;
    }

    @Override
    public FeedSourceTypeEnum matchSourceType() {
        return FeedSourceTypeEnum.STATUS;
    }

    @Override
    public int order() {
        return 0;
    }
}
