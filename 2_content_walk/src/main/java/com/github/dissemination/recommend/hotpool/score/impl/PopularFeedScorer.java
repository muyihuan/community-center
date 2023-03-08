package com.github.dissemination.recommend.hotpool.score.impl;

import com.github.content.ugc.domain.ContentCarrier;
import com.github.dissemination.recommend.hotpool.score.Result;
import com.github.dissemination.recommend.hotpool.score.Scorer;

import java.util.List;
import java.util.Map;

/**
 * popular评分器.
 *
 * @author yanghuan
 */
public class PopularFeedScorer implements Scorer<Object> {

    @Override
    public Result score(String user, ContentCarrier contentCarrier, Object event) {
        return null;
    }

    @Override
    public Map<Long, Result> score(long user, List<ContentCarrier> contentCarriers, Object event) {
        return null;
    }
}
