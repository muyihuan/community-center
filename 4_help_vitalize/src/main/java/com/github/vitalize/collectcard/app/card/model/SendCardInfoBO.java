package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

/**
 * 卡牌赠送次数信息.
 *
 * @author yanghuan
 */
@Data
public class SendCardInfoBO {

    /**
     * 用户uid.
     */
    private String uid;

    /**
     * 今日送卡次数.
     */
    private Integer todaySendCount;

    /**
     * 今日送卡总次数.
     */
    private Integer todayMaxCount;
}
