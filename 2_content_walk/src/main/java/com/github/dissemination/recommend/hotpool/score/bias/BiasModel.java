package com.github.dissemination.recommend.hotpool.score.bias;

import com.github.content.ContentCarrier;

/**
 * 获取偏差值.
 *
 * @author yanghuan
 */
public interface BiasModel {

    /**
     * 获取用户偏差分数.
     *
     * @param user 用户.
     * @return 偏差值.
     */
    double getUserBiasScore(String user);

    /**
     * 获取用户偏差权重.
     *
     * @param user 用户.
     * @return 偏差权重.
     */
    double getUserBiasWeight(String user);

    /**
     * 获取内容的偏差分数.
     *
     * @param contentCarrier 内容载体.
     * @return 偏差值
     */
    double getContentBiasScore(ContentCarrier contentCarrier);

    /**
     * 获取内容的偏差权重.
     *
     * @param contentCarrier 内容载体.
     * @return 偏差权重.
     */
    double getContentBiasWeight(ContentCarrier contentCarrier);
}
