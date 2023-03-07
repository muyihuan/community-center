package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

import java.util.List;

/**
 * 下发卡牌参数.
 *
 * @author yanghuan
 */
@Data
public class DistributeCardsResultBO {

    /**
     * 下发的卡牌.
     */
    private List<CardBO> cardList;

    /**
     * 下发卡后播放的动画地址.
     */
    private String dynamicUrl;

    /**
     * 卡包ID.
     */
    private Long cardBagId;
}
