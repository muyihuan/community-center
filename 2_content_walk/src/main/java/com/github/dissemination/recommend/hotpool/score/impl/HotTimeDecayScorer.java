package com.github.dissemination.recommend.hotpool.score.impl;

import com.github.content.ContentCarrier;
import com.github.dissemination.recommend.hotpool.score.Result;
import com.github.dissemination.recommend.hotpool.score.Scorer;

import java.util.List;
import java.util.Map;

/**
 * 热度评分器-支持时间衰减.
 *
 * @author yanghuan
 */
public class HotTimeDecayScorer implements Scorer<Object> {

    /**
     * 时间衰减重力，衰减到一半需要72小时.
     */
    private static final double G = 0.176;

    /**
     * 时间偏移值.
     */
    private static final double S = 10;

    /**
     * 衰减补齐.
     */
    private static final double K = (1 - 1 / Math.pow(S, G));

    /**
     * 时间保护期.
     */
    private static final double B = 0;

    /**
     * 时间因子单位.
     */
    private static final int ONE_HOUR = 3600000;

    /**
     * 时间衰减函数.
     *
     * @param score 分数.
     * @param timeFactor 时间因素(小时).
     */
    private double timeDecay(Double score, int timeFactor) {
        if(score == null) {
            return 0;
        }

        if(timeFactor <= B) {
            timeFactor = 0;
        }
        else {
            timeFactor -= B;
        }

        timeFactor += S;
        return score * ((1 / (Math.pow(timeFactor, G)) + K * (S / (timeFactor * 1.0))));
    }

    @Override
    public Result score(String user, ContentCarrier contentCarrier, Object event) {
        return null;
    }

    @Override
    public Map<Long, Result> score(long user, List<ContentCarrier> contentCarriers, Object event) {
        return null;
    }
}
