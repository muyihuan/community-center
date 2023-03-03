package com.github.content.feed.app.processor;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;

/**
 * feed创建处理器: 创建UGC前 => 创建FEED前 => 创建FEED后.
 *
 * @author yanghuan
 */
public interface CreateProcessor {

    /**
     * 创建FEED前.
     *
     * @param feedCreateBO feed创建参数.
     */
    void beforeCreateFeed(AbstractFeedCreateBO feedCreateBO);

    /**
     * 创建FEED后.
     *
     * @param feedId feed的ID.
     * @param feedCreateBO feed创建参数.
     */
    void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateBO);

    /**
     * 返回 null 匹配所有.
     */
    FeedContentTypeEnum matchContentType();

    /**
     * 数值越大优先级越低.
     *
     * @return 优先级.
     */
    int order();

    /**
     * 处理流程匹配.
     *
     * @param abstractFeedCreateBO feed创建参数.
     * @return 是否匹配.
     */
    default boolean match(AbstractFeedCreateBO abstractFeedCreateBO) {
        return matchContentType() == null || (matchContentType() == abstractFeedCreateBO.getContentType());
    }
}
