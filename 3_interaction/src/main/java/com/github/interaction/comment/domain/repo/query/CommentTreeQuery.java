package com.github.interaction.comment.domain.repo.query;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.domain.repo.model.CommentDO;

import java.util.List;

/**
 * 评论聚合查询器.
 *
 * @author yanghuan
 */
public interface CommentTreeQuery {

    /**
     * 查询评论树.
     *
     * @param condition 查询条件.
     * @return 评论列表.
     */
    List<CommentDO> query(QueryCondition condition);

    /**
     * 数据清理、缓存.
     *
     * @param topicType 宿主类型.
     * @param topicId 宿主ID.
     */
    void clean(TopicTypeEnum topicType, Long topicId);
}
