package com.github.interaction.comment.app.commet.model;

import com.github.interaction.comment.app.commet.enums.CommentVisibilityEnum;
import com.github.interaction.comment.app.commet.enums.ContentTypeEnum;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 评论创建通用参数.
 *
 * @author yanghuan
 */
@Data
@NoArgsConstructor
public abstract class AbstractCommentCreateBO {

    /**
     * 发布的用户ID.
     */
    private String uid;

    /**
     * 评论的宿主类型.
     */
    private TopicTypeEnum topicType;

    /**
     * 评论的宿主的ID
     */
    private Long topicId;

    /**
     * 被回复的评论
     */
    private Long repliedCommentId;

    /**
     * 主评论的ID
     */
    private Long rootCommentId;

    /**
     * 文字内容
     */
    private String textContent;

    /**
     * 可见性设置
     */
    private CommentVisibilityEnum visibility;

    /**
     * 是否是系统发布的，系统发布的会省略一些流程处理；例如：审核
     */
    private Boolean isSystemComment;

    /**
     * 系统参数
     */
    protected SystemParam systemParam;

    /**
     * 来源，例如广场、朋友圈、话题里发布的评论
     */
    protected String sourceFrom;

    /**
     * 扩展信息
     */
    private Map<String, Object> extraInfo;

    /**
     * 评论内容类型
     */
    @Setter(AccessLevel.NONE)
    protected ContentTypeEnum contentType;

    /**
     * 子类必须设置 contentType
     *
     * @param contentType {@link ContentTypeEnum}
     */
    public AbstractCommentCreateBO(ContentTypeEnum contentType) {
        this.contentType = contentType;
        this.visibility = CommentVisibilityEnum.DEF;
        this.isSystemComment = false;
    }
}