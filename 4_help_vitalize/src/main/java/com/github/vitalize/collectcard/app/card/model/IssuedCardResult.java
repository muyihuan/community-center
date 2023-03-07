package com.github.vitalize.collectcard.app.card.model;

import com.github.vitalize.collectcard.domains.cardmeta.model.CardModel;
import lombok.Data;

import java.util.List;

/**
 * 下发卡结果
 * @author yanghuan
 */
@Data
public class IssuedCardResult {

    /**
     * 开出来的卡牌
     */
    private List<CardModel> cardList;

    /**
     * 卡包的ID
     */
    private Long cardBagId;

    /**
     * 失败原因
     */
    private String  failMsg;
}
