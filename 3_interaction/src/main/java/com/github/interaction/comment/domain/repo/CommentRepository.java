package com.github.interaction.comment.domain.repo;

import com.github.interaction.comment.app.CommentService;
import com.github.interaction.comment.app.commet.enums.CommentVisibilityEnum;
import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.app.commet.model.QueryTopicCommentListParam;
import com.github.interaction.comment.domain.enums.CommentStateEnum;
import com.github.interaction.comment.domain.repo.model.CommentDO;
import com.github.interaction.comment.domain.repo.query.QueryCondition;
import com.github.interaction.comment.domain.repo.query.QueryType;
import com.github.interaction.comment.exception.CommentException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 评论存储/计算/缓存处理.
 *
 * @author yanghuan
 */
@Service
public class CommentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    private static ExecutorService executorService = null;

    @Autowired
    private QueryFactory queryFactory;

    private static final String COMMENT_CACHE_KEY = "comment:cache:key:";

    private static final int EXPIRE_TIME = (int) TimeUnit.DAYS.toSeconds(1);

    /**
     * 存储评论.
     *
     * @param comment 评论信息
     */
    public void saveComment(CommentDO comment) {
        if(comment == null || comment.getCommentId() == null) {
            throw new CommentException();
        }

        // 保存数据通过 评论宿主ID 进行分表
    }

    /**
     * 获取评论信息.
     *
     * @param commentId 评论唯一ID
     */
    public CommentDO getComment(Long commentId) {
        if(commentId == null) {
            throw new CommentException();
        }

        return null;
    }

    /**
     * 删除评论.
     *
     * @param commentId 评论唯一ID
     */
    public void deleteComment(Long commentId) {
        if(commentId == null) {
            throw new CommentException();
        }


    }

    /**
     * 通过评论宿主查询评论列表，只缓存第一页.
     *
     * @param paramList 查询参数.
     * @return key: topicType_topicId value: 评论列表.
     */
    public Map<String, List<CommentDO>> batchQueryCommentTreeByTopic(List<QueryTopicCommentListParam> paramList, QueryType queryType) {
        if(CollectionUtils.isEmpty(paramList)) {
            return Collections.emptyMap();
        }

        Map<String, List<CommentDO>> result = new ConcurrentHashMap<>(paramList.size());
        List<CompletableFuture> futures = new ArrayList<>();
        for(QueryTopicCommentListParam param : paramList) {
            CompletableFuture future = CompletableFuture.runAsync(() -> {
                QueryCondition condition = new QueryCondition();
                condition.setQueryType(queryType == null ? QueryType.TIME_SORT_DESC : queryType);
                condition.setTopicType(param.getTopicType());
                condition.setTopicId(param.getTopicId());
                if(param.getLastCommentId() == null || param.getLastCommentId() == 0) {
                    condition.setLastId(null);
                }
                else {
                    CommentDO commentDO = getComment(param.getLastCommentId());
                    if(commentDO == null) {
                        return;
                    }
                    condition.setLastId(commentDO.getId());
                }
                condition.setCount(param.getCount());
                List<CommentDO> commentDOList = queryFactory.getQuery(condition).query(condition);

                result.put(param.getTopicType() + "_" + param.getTopicId(), commentDOList);
            }, executorService);

            futures.add(future);
        }

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(500, TimeUnit.MILLISECONDS);
        }
        catch (Exception e) {
            LOGGER.info("act=batchQueryCommentTreeByTopic timeout", e);
        }

        return result;
    }

    /**
     * 通过评论宿主和评论作者列表查询评论列表，无缓存.
     */
    public List<CommentDO> queryCommentTreeByTopicAndUsers(TopicTypeEnum topicType, Long topicId, Integer count, List<String> uidList) {
        if(topicType == null || topicId == null || CollectionUtils.isEmpty(uidList)) {
            throw new CommentException();
        }

        QueryCondition condition = new QueryCondition();
        condition.setQueryType(QueryType.BY_USER);
        condition.setTopicType(topicType);
        condition.setTopicId(topicId);
        condition.setCount(count);
        condition.setCommentUidList(uidList);
        return queryFactory.getQuery(condition).query(condition);
    }

    /**
     * 更新评论的可见性.
     */
    public void updateCommentVisibility(Long commentId, CommentVisibilityEnum visibility) {
        if(commentId == null || visibility == null) {
            throw new CommentException();
        }

    }

    /**
     * 更新评论的状态.
     */
    public void updateCommentStatus(Long commentId, CommentStateEnum state) {
        if(commentId == null || state == null) {
            throw new CommentException();
        }

    }

    /**
     * 更新评论的热度值.
     */
    public void updateHeatScore(Long commentId, Long changeScore) {
        if(changeScore == null || changeScore == 0) {
            return;
        }

    }

    /**
     * 查询评论宿主下的全部人可见的评论数.
     */
    public Integer getTopicCommentCount(Integer topicType, Long topicId) {
        if(topicType == null || topicId == null) {
            throw new CommentException();
        }

        return 0;
    }

    /**
     * 按热度分值查询评论宿主下的全部人可见的评论.
     */
    public List<CommentDO> getTopicCommentByHeat(Integer topicType, Long topicId, Long minHeat, Integer offset, Integer count) {
        if(topicType == null || topicId == null || minHeat == null || offset == null || count == null) {
            throw new CommentException();
        }

        return null;
    }

    /**
     * 刷新评论缓存 先清空旧缓存 => 刷新新数据.
     */
    private void refresh(Long commentId) {
        CommentDO commentDO = getComment(commentId);
        queryFactory.clean(commentDO.getTopicBizType(), commentDO.getTopicBizId());

        executorService.execute(() -> {
            QueryCondition condition = new QueryCondition();
            condition.setQueryType(QueryType.TIME_SORT_DESC);
            condition.setTopicType(TopicTypeEnum.getByCode(commentDO.getTopicBizType()));
            condition.setTopicId(commentDO.getTopicBizId());
            condition.setLastId(0L);
            condition.setCount(30);
            queryFactory.getQuery(condition).query(condition);

            condition.setQueryType(QueryType.TIME_SORT_ASC);
            queryFactory.getQuery(condition).query(condition);
        });
    }

    private static void deleteCommentCache(Long commentId) {

    }

    /**
     * 评论信息缓存.
     */
    private static String getCommentCacheKey(Long commentId) {
        return COMMENT_CACHE_KEY + commentId;
    }
}
