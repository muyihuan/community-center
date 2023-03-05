package com.github.content.feed.domain.repo.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * feed存储元信息.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class FeedDO implements Serializable {

    /**
     * feed的ID.
     */
    private Long feedId;

    /**
     * feed的类型.
     */
    private Integer feedType;

    /**
     * feed的作者.
     */
    private String feedUid;

    /**
     * feed的可见度（0：好友可见、1：全部可见、2：仅陌生人可见）.
     */
    private Integer privilege;

    /**
     * @ 信息.
     */
    private String atInfo;

    /**
     * 内容数据.
     */
    private String content;

    /**
     * 来源场景对应的ID.
     */
    private String tags;

    /**
     * feed的状态.
     */
    private Integer state;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 来源场景.
     */
    private String sourceFrom;

    /**
     * 扩展信息.
     */
    private String extraInfo;
}
