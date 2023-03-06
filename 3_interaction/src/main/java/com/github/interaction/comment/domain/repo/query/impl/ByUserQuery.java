package com.github.interaction.comment.domain.repo.query.impl;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.domain.repo.model.CommentDO;
import com.github.interaction.comment.domain.repo.query.CommentTreeQuery;
import com.github.interaction.comment.domain.repo.query.QueryCondition;

import java.util.List;

/**
 * 按时间顺序查询.
 *
 * @author yanghuan
 */
public class ByUserQuery implements CommentTreeQuery {

    @Override
    public List<CommentDO> query(QueryCondition condition) {
        return queryCommentList(condition.getTopicType(), condition.getTopicId(), condition.getLastId(), condition.getCommentUidList(), condition.getCount());
    }

    @Override
    public void clean(TopicTypeEnum topicType, Long topicId) {
    }

    private List<CommentDO> queryCommentList(TopicTypeEnum topicType, Long topicId, Long lastId, List<String> commentUidList, Integer count) {
        return null;
    }
}
