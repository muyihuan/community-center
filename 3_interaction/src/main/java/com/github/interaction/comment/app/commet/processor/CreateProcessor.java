package com.github.interaction.comment.app.commet.processor;

import com.github.interaction.comment.app.commet.model.AbstractCommentCreateBO;

/**
 * 创建评论处理流程.
 *
 * @author yanghuan
 */
public interface CreateProcessor {

    /**
     * 优先级.
     *
     * @return 整数.
     */
    int order();

    /**
     * 创建评论前.
     *
     * @param commentCreateBO 参数.
     */
    void beforeCreateComment(AbstractCommentCreateBO commentCreateBO);

    /**
     * 创建评论后.
     *
     * @param commentId 评论ID.
     * @param commentCreateBO 参数.
     */
    void afterCreateComment(Long commentId, AbstractCommentCreateBO commentCreateBO);
}
