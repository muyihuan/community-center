package com.github.content.feed.app.checker;

/**
 * 创建处理器，可以对创建UGC前、创建FEED前、创建 FEED 后进行扩展.
 *
 * @author yanghuan
 */
public interface Checker {

    /**
     * 创建UGC前
     * @param feedCreateModel 参数
     * @return 返回
     */
    CheckResult check(AbstractFeedCreateBO feedCreateModel);

    /**
     * 返回 null 匹配所有
     * @return 返回
     */
    FeedContentTypeEnum matchContentType();

    /**
     * 数值越大优先级越低
     * @return 返回
     */
    int order();
}
