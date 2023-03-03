package com.github.content.feed.domain.model;

import com.github.content.feed.domain.enums.*;
import com.github.content.feed.domain.model.property.AtInfo;
import com.github.content.feed.domain.model.property.ExtraInfo;
import com.github.content.feed.domain.model.property.LbsInfo;
import com.github.content.feed.domain.model.ugc.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * feed实体模型.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class FeedModel {

    /**
     * feed的ID.
     */
    private Long feedId;

    /**
     * feed的作者.
     */
    private String feedUid;

    /**
     * feed的类型.
     */
    private FeedContentTypeEnum contentType;

    /**
     * 可见范围，是用户侧设置的可见范围，优先级小于系统可见范围.
     */
    private FeedPrivilegeEnum privilege;

    /**
     * 系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围.
     */
    private FeedSystemPrivilegeEnum systemPrivilege;

    /**
     * feed的状态.
     */
    private FeedStateEnum state;

    /**
     * 来源场景类型.
     */
    private FeedSourceTypeEnum sourceType;

    /**
     * 来源场景对应的ID.
     */
    private String sourceId;

    /**
     * 话题信息.
     */
    private List<Long> tagIds;

    /**
     * @ 信息.
     */
    private List<AtInfo> atInfoList;

    /**
     * feed的坐标信息信息（经纬度）.
     */
    private LbsInfo lbsInfo;

    /**
     * 文字内容.
     */
    private TextContent text;

    /**
     * 图片内容.
     */
    private List<ImageContent> imageList;

    /**
     * 链接内容.
     */
    private LinkContent link;

    /**
     * 视频内容.
     */
    private VideoContent video;

    /**
     * 转发内容.
     */
    private RepostContent repost;

    /**
     * 音频内容.
     */
    private AudioContent audio;

    /**
     * 扩展.
     */
    private ExtraInfo extraInfo;

    /**
     * 创建时间
     */
    private Date createTime;
}
