package com.github.dissemination.recommend.easyrec.score.impl;

import com.github.dissemination.recommend.easyrec.score.Scorer;
import com.github.dissemination.recommend.easyrec.score.enums.ScorerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 评分器的工厂类.
 *
 * @author yanghuan
 */
public class ScorerFactory {

    private static Map<ScorerType, Scorer> SCORETR_MAP = new ConcurrentHashMap<>();

    /**
     * 获取评分器
     * @param type 评分器类型
     */
    public static Scorer getScorer(ScorerType type) {
        if(type == null) {
            return null;
        }

        synchronized (type) {
            Scorer scorer = SCORETR_MAP.get(type);
            if(scorer != null) {
                return scorer;
            }

            scorer = getScorerByType(type);
            SCORETR_MAP.put(type, scorer);
            return scorer;
        }
    }

    private static Scorer getScorerByType(ScorerType type) {
        switch (type) {
            case BASE_SCORE_SCORER:
                return new BaseScoreScorer();
            case INTERACTIVE_SCORER :
                return new PopularFeedScorer();
            case TIME_DECAY_SCORER :
                return new HotTimeDecayScorer();
            default:
                throw new RuntimeException("暂不支持该类型评分器");
        }
    }
}
