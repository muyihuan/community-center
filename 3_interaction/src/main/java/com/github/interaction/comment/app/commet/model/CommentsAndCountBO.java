package com.github.interaction.comment.app.commet.model;

import lombok.Data;

import java.util.List;

/**
 * 评论和评论数汇总.
 *
 * @author yanghuan
 */
@Data
public class CommentsAndCountBO {

    /**
     * 评论列表.
     */
    List<CommentBO> commentBOList;

    /**
     * 评论数.
     */
    private Long allCommentsCount;
}
