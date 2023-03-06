package com.github.interaction.comment.app.commet.model;

import com.github.interaction.comment.app.commet.enums.CommentVisibilityEnum;
import com.github.interaction.comment.app.commet.enums.ContentTypeEnum;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.domain.model.AudioInfo;
import com.github.interaction.comment.domain.model.ImageInfo;
import com.github.interaction.comment.domain.model.TextInfo;
import lombok.Data;

import java.util.Date;

/**
 * 评论.
 *
 * @author yanghuan
 */
@Data
public class CommentBO {

    /***********************************************/
    /********************基础属性*********************/
    /***********************************************/

    /**
     * 评论的唯一标识.
     */
    private Long commentId;

    /**
     * 评论的作者信息.
     */
    private String commentUid;

    /**
     * 评论宿主的类型.
     */
    private TopicTypeEnum topicType;

    /**
     * 评论宿主的ID.
     */
    private Long topicId;

    /**
     * 内容类型：文本、图片、语音.
     */
    private ContentTypeEnum contentType;

    /**
     * 文字信息.
     */
    private TextInfo textInfo;

    /**
     * 图片内容.
     */
    private ImageInfo imageInfo;

    /**
     * 语音内容.
     */
    private AudioInfo audioInfo;

    /**
     * 是否是热评.
     */
    private Boolean isHot;

    /**
     * 可见范围：0:全部人可见，1:仅自己可见.
     */
    private CommentVisibilityEnum visibility;

    /**
     * 创建时间.
     */
    private Date createTime;

    /***********************************************/
    /*****************回复用户相关信息******************/
    /***********************************************/

    /**
     * 被回复的评论ID.
     */
    private Long repliedCommentId;

    /**
     * 被回复的用户信息.
     */
    private String repliedUid;

    /**
     * 业务扩展.
     */
    private ExtraInfo extInfo;
}
