package com.github.content.feed.app.processor;

/**
 * 创建处理器，可以对创建UGC前、创建FEED前、创建 FEED 后进行扩展.
 *
 * @author yanghuan
 */
public interface CreateProcessor {

    /**
     * 创建UGC前
     * @param feedCreateModel 参数
     */
    void beforeCreateUgc(AbstractFeedCreateBO feedCreateModel);

    /**
     * 创建FEED前
     * @param feedCreateModel 参数
     */
    void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel);

    /**
     * 创建FEED后
     * @param feedId feedid
     * @param feedCreateModel 参数
     */
    void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel);

    /**
     * 返回 null 匹配所有
     * @return
     */
    FeedContentTypeEnum matchContentType();

    /**
     * 返回 null 匹配所有
     * @return
     */
    FeedSourceTypeEnum matchSourceType();

    /**
     * 数值越大优先级越低
     * @return 返回
     */
    int order();

    /**
     * 匹配
     * @param abstractFeedCreateBO
     * @return
     */
    default boolean match(AbstractFeedCreateBO abstractFeedCreateBO) {
        boolean contentTypeMatch;
        boolean sourceTypeMatch;
        if (matchContentType() == null) {
            contentTypeMatch = true;
        } else {
            contentTypeMatch = matchContentType() == abstractFeedCreateBO.getContentType();
        }
        if (matchSourceType() == null) {
            sourceTypeMatch = true;
        } else {
            sourceTypeMatch = matchSourceType() == abstractFeedCreateBO.getSourceType();
        }
        return contentTypeMatch && sourceTypeMatch;
    }
}
