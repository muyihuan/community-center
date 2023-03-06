package com.github.interaction.comment.domain.model;

import com.github.interaction.comment.domain.enums.InteractionTypeEnum;
import lombok.Data;

/**
 * 评论互动信息
 * @author yanghuan
 */
@Data
public class CommentInteractionInfo {

    /**
     * 类型
     */
    private InteractionTypeEnum type;

    /**
     * 用户
     */
    private String uid;

    /**
     * 次数或者个数
     */
    private Long count;

    /**
     * 评论ID
     */
    private Long commentId;
}
