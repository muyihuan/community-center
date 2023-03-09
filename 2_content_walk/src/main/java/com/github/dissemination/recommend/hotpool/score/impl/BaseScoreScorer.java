package com.github.dissemination.recommend.hotpool.score.impl;

import com.github.content.ContentCarrier;
import com.github.dissemination.recommend.hotpool.score.Result;
import com.github.dissemination.recommend.hotpool.score.Scorer;
import com.github.dissemination.recommend.hotpool.score.bias.CommonBiasModel;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 基础分值的评分器.
 *
 * @author yanghuan
 */
public class BaseScoreScorer implements Scorer<Double> {

    private CommonBiasModel biasModel = new CommonBiasModel();

    @Override
    public Result score(String user, ContentCarrier contentCarrier, Double defScore) {
        if(StringUtils.isEmpty(user) || contentCarrier == null) {
            throw new RuntimeException("获取评分user和feed不可以为空");
        }

        defScore = defScore == null ? 0.0 : defScore;
        Double baseScore = defScore + biasModel.getUserBiasScore(user) + biasModel.getContentBiasScore(contentCarrier);
        return new Result(user, contentCarrier, baseScore);
    }

    @Override
    public Map<Long, Result> score(long user, List<ContentCarrier> contentCarriers, Double defScore) {
        throw new RuntimeException("暂不支持批量评分");
    }
}
