package com.github.dissemination.recommend.hotpool.score;

import com.github.content.ContentCarrier;

import java.util.List;
import java.util.Map;

/**
 * 评分器.
 *
 * @author yanghuan
 */
public interface Scorer<T> {

    /**
     * 给内容评分.
     *
     * @param user 用户.
     * @param contentCarrier 内容载体.
     * @param event 业务特有.
     * @return 评分结果.
     */
    Result score(String user, ContentCarrier contentCarrier, T event);

    /**
     * 给内容评分.
     *
     * @param user 用户.
     * @param contentCarriers 内容载体.
     * @param event 业务特有
     * @return 评分结果.
     */
    Map<Long, Result> score(long user, List<ContentCarrier> contentCarriers, T event);
}
