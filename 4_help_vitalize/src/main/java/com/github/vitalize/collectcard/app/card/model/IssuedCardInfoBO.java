package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

/**
 * 卡牌下发次数信息.
 *
 * @author yanghuan
 */
@Data
public class IssuedCardInfoBO {

    /**
     * 用户.
     */
    private String uid;

    /**
     * 今日下发次数.
     */
    private Integer todayIssuedCount;

    /**
     * 今日总次数.
     */
    private Integer todayMaxCount;
}
