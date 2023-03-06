package com.github.interaction.comment.domain.repo.query.impl;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.domain.repo.model.CommentDO;
import com.github.interaction.comment.domain.repo.query.CommentTreeQuery;
import com.github.interaction.comment.domain.repo.query.QueryCondition;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 按时间顺序查询.
 *
 * @author yanghuan
 */
public class TimeOrderQuery implements CommentTreeQuery {

    private static final String TIME_TOPIC_COMMENTS_CACHE_KEY = "time:topic:comments:key:";

    private static final int EXPIRE_TIME = (int) TimeUnit.DAYS.toSeconds(1);

    private boolean isAsc;

    public TimeOrderQuery(boolean asc) {
        this.isAsc = asc;
    }

    @Override
    public List<CommentDO> query(QueryCondition condition) {
        List<CommentDO> commentList = null;

        if(condition.getLastId() == null || condition.getLastId() == 0L) {
            String cacheKey = getTopicCommentsCacheKey(condition.getTopicType().getType(), condition.getTopicId());
            // 缓存.
        }
        else {
            commentList = queryCommentList(condition.getTopicType(), condition.getTopicId(), condition.getLastId(), condition.getCount());
        }

        if(CollectionUtils.isNotEmpty(commentList)) {
            if(commentList.size() > condition.getCount()) {
                commentList = commentList.subList(0, condition.getCount());
            }
        }

        return commentList;
    }

    @Override
    public void clean(TopicTypeEnum topicType, Long topicId) {
    }

    private List<CommentDO> queryCommentList(TopicTypeEnum topicType, Long topicId, Long lastId, Integer count) {
        return null;
    }

    /**
     * 评论宿主的评论列表缓存
     */
    private String getTopicCommentsCacheKey(Integer topicType, Long topicId) {
        return TIME_TOPIC_COMMENTS_CACHE_KEY + ":" + isAsc + ":" + topicType + ":" + topicId;
    }
}
