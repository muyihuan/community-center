package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

import java.util.Date;

/**
 * 赠卡订单实体.
 *
 * @author yanghuan
 */
@Data
public class SendCardOrderModel {

    /**
     * 订单ID.
     */
    private Long id;

    /**
     * 送卡人.
     */
    private String senderUid;

    /**
     * 收卡人.
     */
    private String receiverUid;

    /**
     * 卡牌ID.
     */
    private Long cardId;

    /**
     * 过期时间.
     */
    private Date expireTime;

    /**
     * 创建时间.
     */
    private Date createTime;
}
