package com.github.interaction.comment.domain;

import com.github.interaction.comment.app.commet.enums.TopicTypeEnum;
import com.github.interaction.comment.domain.repo.CommentRepository;
import com.github.interaction.comment.exception.CommentException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 评论计数处理.
 *
 * @author yanghuan
 */
@Service
public class CommentCounterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentCounterService.class);

    private static ExecutorService executorService = null;

    /**
     * 用户自见的评论，单独计数，不算做评论宿主的评论数里.
     */
    private static final String SELF_VIEW_COMMENT_COUNT_CACHE_KEY = "comment:count:key:";

    /**
     * 自见数目前只维护一周的数据，目前自见的数据不多，自见时间也不长，之后业务有更多要求的话，可自行扩展.
     */
    private static final int SELF_VIEW_COMMENT_EXPIRE_TIME = (int) TimeUnit.DAYS.toSeconds(7);

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 获取评论数 = 用户自见的评论数(自见数目前只维护一周的数据，后续业务要求高的话另做处理，目前自见不多) + 所有人可见的评论数.
     *
     * @param topicType 评论宿主类型.
     * @param topicIdList 评论宿主ID集合.
     */
    public Map<Long, Long> batchGetCommentCount(String user, TopicTypeEnum topicType, List<Long> topicIdList) {
        if(topicType == null || topicIdList == null) {
            throw new CommentException();
        }

        if(CollectionUtils.isEmpty(topicIdList)) {
            return Collections.emptyMap();
        }

        // 公开评论数
        Map<String, Long> countersMap = null;
        // 自见评论数
        Map<Long, Long> selfCountMap = batchGetUserSelfCommentCount(user, topicType, topicIdList);

        Map<Long, Long> result = new HashMap<>(topicIdList.size());
        List<Long> needRefreshIds = new ArrayList<>(topicIdList.size());
        for(Long id : topicIdList) {
            Long count = MapUtils.getLong(countersMap, getCounterKey(topicType, id));
            if(count == null) {
                needRefreshIds.add(id);
                continue;
            }

            Long selfCount = MapUtils.isEmpty(selfCountMap) ? 0L : selfCountMap.get(id);
            result.put(id, count + (selfCount == null ? 0L : selfCount));
        }

        if(CollectionUtils.isNotEmpty(needRefreshIds)) {
            // 老数据可能还没有同步计数，为了迁移的老数据同步计数
            syncCommentCounter(topicType, needRefreshIds);
        }

        return result;
    }

    /**
     * 增加评论数 1.
     *
     * @param topicType 评论宿主类型.
     * @param topicId 评论宿主ID.
     */
    public void incrCommentCount(TopicTypeEnum topicType, Long topicId) {
        if(topicType == null || topicId == null) {
            throw new CommentException();
        }


        LOGGER.info("act=incrCommentCount topicType={} topicId={}", topicType, topicId);
    }

    /**
     * 减少评论数 1.
     *
     * @param topicType 评论宿主类型.
     * @param topicId 评论宿主ID.
     */
    public void decrCommentCount(TopicTypeEnum topicType, Long topicId) {
        if(topicType == null || topicId == null) {
            throw new CommentException();
        }

        LOGGER.info("act=decrCommentCount topicType={} topicId={}", topicType, topicId);
    }

    /**
     * 获取用户自见的评论数.
     *
     * @param topicType 评论宿主类型.
     * @param topicIdList 评论宿主ID集合.
     */
    private Map<Long, Long> batchGetUserSelfCommentCount(String user, TopicTypeEnum topicType, List<Long> topicIdList) {
        if(StringUtils.isEmpty(user) || topicType == null || topicIdList == null) {
            return Collections.emptyMap();
        }

        return null;
    }

    /**
     * 增加用户自见评论数 1.
     *
     * @param topicType 评论宿主类型.
     * @param topicId 评论宿主ID.
     */
    public static void incrUserSelfCommentCount(String user, TopicTypeEnum topicType, Long topicId) {
        if(topicType == null || topicId == null) {
            throw new CommentException();
        }

        LOGGER.info("act=incrUserSelfCommentCount user={} topicType={} topicId={}", user, topicType, topicId);
    }

    /**
     * 减少用户自见评论数 1.
     *
     * @param topicType 评论宿主类型.
     * @param topicId 评论宿主ID.
     */
    public static void decrUserSelfCommentCount(String user, TopicTypeEnum topicType, Long topicId) {
        if(topicType == null || topicId == null) {
            throw new CommentException();
        }

        LOGGER.info("act=decrUserSelfCommentCount user={} topicType={} topicId={}", user, topicType, topicId);
    }

    /**
     * 同步计数服务.
     *
     * 如何保证计数服务的评论数尽力一致的方式：
     * 1. 在往计数服务同步期间，发生的任何变化都要再发起一次同步，如果一致性性要求更高时可以加锁，防止乱序了.
     * 2. 一个频率校验计数服务的数值是否正确，不正确再同步一次.
     */
    private void syncCommentCounter(TopicTypeEnum topicType, List<Long> topicIdList) {
        executorService.execute(() -> {

        });
    }

    /**
     * 用户自见评论数缓存key.
     */
    private static String getUserSelfCommentCountCacheKey(String user, TopicTypeEnum topicType, Long topicId) {
        return SELF_VIEW_COMMENT_COUNT_CACHE_KEY + user + ":" + topicType.getType() + ":" + topicId;
    }

    private static String getCounterKey(TopicTypeEnum topicType, Long topicId) {
        return topicType.getType() + "_" + topicId;
    }
}
