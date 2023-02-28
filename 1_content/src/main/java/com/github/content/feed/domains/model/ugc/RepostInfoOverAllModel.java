package com.github.content.feed.domains.model.ugc;

import lombok.Data;

/**
 * 转发更全面的信息
 * @author yanghuan
 */
@Data
public class RepostInfoOverAllModel {

    /**
     * 根feed不可以是转发类型feed
     */
    private FeedSourceModel rootFeedSource;

    /**
     * 转发原始信息
     */
    private RepostInfoContentModel repostUgcContent;
}
