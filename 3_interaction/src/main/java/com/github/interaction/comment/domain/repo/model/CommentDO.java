package com.github.interaction.comment.domain.repo.model;

import lombok.Data;

import java.util.Date;

/**
 * 评论存储元数据.
 *
 * @author yanghuan
 */
@Data
public class CommentDO {

    /**
     * 数据库主键.
     */
    private Long id;

    /**
     * 评论的唯一标识，主键.
     */
    private Long commentId;

    /**
     * 评论的唯一标识.
     */
    private String commentUid;

    /**
     * 内容类型：文本、图片、语音.
     */
    private Integer contentType;

    /**
     * 用户填写的内容 jsonString.
     */
    private String content;

    /**
     * 评论宿主的类型.
     */
    private Integer topicBizType;

    /**
     * 评论宿主的ID.
     */
    private Long topicBizId;

    /**
     * 被回复的评论ID.
     */
    private Long repliedCommentId;

    /**
     * 主评论的ID.
     */
    private Long rootCommentId;

    /**
     * 热度 热度越高越受欢迎 整数增长.
     */
    private Long heatScore;

    /**
     * 0:正常，1:不可用(删除).
     */
    private Integer status;

    /**
     * 可见范围：0:全部人可见，1:仅自己可见.
     */
    private Integer visibility;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 额外信息 例如：用户昵称头像、评论皮肤等.
     */
    private String extraInfo;
}
