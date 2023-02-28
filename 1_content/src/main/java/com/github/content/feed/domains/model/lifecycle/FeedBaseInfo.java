package com.github.content.feed.domains.model.lifecycle;

import com.github.content.feed.domains.enums.FeedSourceTypeEnum;
import com.github.content.feed.domains.model.property.AtInfo;

import java.util.Date;
import java.util.List;

/**
 * feed基础信息.
 *
 * @author yanghuan
 */
public class FeedBaseInfo {

    /**
     * feed的创建时间
     */
    private Date feedCreateTime;

    /**
     * ugc内容来源哪里
     */
    private FeedSourceTypeEnum sourceType;

    /**
     * feed携带的话题信息
     */
    private List<Long> tagIdList;

    /**
     * 艾特信息
     */
    private List<AtInfo> atInfoList;
}
