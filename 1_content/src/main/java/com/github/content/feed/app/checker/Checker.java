package com.github.content.feed.app.checker;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import com.github.content.feed.domain.enums.FeedContentTypeEnum;

/**
 * feed创建校验.
 *
 * @author yanghuan
 */
public interface Checker {

    /**
     * feed创建前置校验.
     *
     * @param feedCreateBO 创建数据.
     * @return 校验结果.
     */
    CheckResult check(AbstractFeedCreateBO feedCreateBO);

    /**
     * 返回 null 匹配所有.
     *
     * @return feed类型.
     */
    FeedContentTypeEnum matchContentType();

    /**
     * 数值越大优先级越低.
     *
     * @return 优先级.
     */
    int order();
}
