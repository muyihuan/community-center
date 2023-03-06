package com.github.interaction.comment.domain.repo;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.domain.repo.query.CommentTreeQuery;
import com.github.interaction.comment.domain.repo.query.QueryCondition;
import com.github.interaction.comment.domain.repo.query.QueryType;
import com.github.interaction.comment.domain.repo.query.impl.ByUserQuery;
import com.github.interaction.comment.domain.repo.query.impl.TimeOrderQuery;
import org.springframework.stereotype.Service;

/**
 * 查询器管理.
 *
 * @author yanghuan
 */
@Service
public class QueryFactory {

    public CommentTreeQuery getQuery(QueryCondition condition) {
        if(condition == null) {
            throw new RuntimeException("查询条件不可为空");
        }

        if(condition.getQueryType() == QueryType.TIME_SORT_ASC) {
            return new TimeOrderQuery(true);
        }
        else if(condition.getQueryType() == QueryType.TIME_SORT_DESC) {
            return new TimeOrderQuery(false);
        }
        else if(condition.getQueryType() == QueryType.BY_USER) {
            return new ByUserQuery();
        }

        throw new RuntimeException("暂不支持该种类型查询");
    }

    public void clean(Integer topicType, Long topicId) {
        if(topicType == null || topicId == null) {
            return;
        }

        new TimeOrderQuery(true).clean(TopicTypeEnum.getByCode(topicType), topicId);
        new TimeOrderQuery(false).clean(TopicTypeEnum.getByCode(topicType), topicId);
        new ByUserQuery().clean(TopicTypeEnum.getByCode(topicType), topicId);
    }
}
