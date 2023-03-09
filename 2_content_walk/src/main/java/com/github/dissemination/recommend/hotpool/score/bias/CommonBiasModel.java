package com.github.dissemination.recommend.hotpool.score.bias;

import com.github.content.ContentCarrier;

/**
 * 通用偏差值
 * @author yanghuan
 */
public class CommonBiasModel implements BiasModel {

    @Override
    public double getUserBiasScore(String user) {
        return 0.0;
    }

    @Override
    public double getUserBiasWeight(String user) {
        return 0.0;
    }

    @Override
    public double getContentBiasScore(ContentCarrier contentCarrier) {
        return 0.0;
    }

    @Override
    public double getContentBiasWeight(ContentCarrier contentCarrier) {
        return 0.0;
    }
}
