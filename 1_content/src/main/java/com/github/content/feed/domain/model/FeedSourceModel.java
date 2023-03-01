package com.github.content.feed.domain.model;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import com.github.content.feed.domain.enums.FeedPrivilegeEnum;
import com.github.content.feed.domain.enums.FeedSourceTypeEnum;
import com.github.content.feed.domain.enums.FeedSystemPrivilegeEnum;
import com.github.content.feed.domain.model.property.AtInfo;
import com.github.content.feed.domain.model.property.ExtraInfo;
import com.github.content.feed.domain.model.property.LbsInfo;
import com.github.content.feed.domain.model.ugc.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * feed模型
 * @author yanghuan
 */
@Data
public class FeedSourceModel {

    /**
     * feed的id
     */
    private Long feedId;

    /**
     * feed的作者
     */
    private String uid;

    /**
     * feed的内容类型
     */
    private FeedContentTypeEnum contentType;

    /**
     * feed的状态
     */
    private FeedStateEnum state;

    /**
     * 可见范围，是用户侧设置的可见范围，优先级小于系统可见范围
     */
    private FeedPrivilegeEnum privilege;

    /**
     * 系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围
     */
    private FeedSystemPrivilegeEnum systemPrivilege;

    /**
     * feed对应的资源id
     */
    private Long sourceId;

    /**
     * feed对应的资源类型
     */
    private FeedSourceTypeEnum sourceType;

    /**
     * feed的中文字内容
     */
    private String textContent;

    /**
     * feed的创建时间
     */
    private Date feedCreateTime;

    /**
     * feed的坐标信息信息（经纬度）
     */
    private LbsInfo lbsInfo;

    /**
     * 是否是匿名feed，0：不是、1：是匿名feed
     */
    private Integer anonymousFeedType;

    /**
     * feed礼物面板配置
     */
    private Integer giftStrategyId;

    /**
     * 文字内容信息
     */
    private TextContentModel text;

    /**
     * 话题信息
     */
    private List<Long> tagIds;

    /**
     * 艾特信息
     */
    private List<AtInfo> atInfoList;

    /**
     * 照片信息
     */
    private AlbumImageContentModel albumImage;

    /**
     * 多图信息
     */
    private MultiImageContentModel multiImage;

    /**
     * 网页链接信息
     */
    private LinkContentModel webLink;

    /**
     * 视频feed
     */
    private VideoContentModel video;

    /**
     * 转发信息
     */
    private RepostInfoOverAllModel repost;

    /**
     * 语音feed
     */
    private AudioContentModel audio;

    /**
     * feed扩展
     */
    private ExtraInfo extraInfo;

    /**
     * 兼容老逻辑使用的
     */
    @Deprecated
    private String ugcContent;
}
