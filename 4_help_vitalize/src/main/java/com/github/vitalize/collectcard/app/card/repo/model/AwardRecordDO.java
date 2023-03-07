package com.github.vitalize.collectcard.app.card.repo.model;

import lombok.Data;

import java.util.Date;

/**
 * 奖励记录.
 *
 * @author yanghuan
 */
@Data
public class AwardRecordDO {

    /**
     * ID.
     */
    private Long id;

    /**
     * 得奖励人.
     */
    private String uid;

    /**
     * 奖励信息.
     */
    private String awardInfo;

    /**
     * 订单ID.
     */
    private String orderId;

    /**
     * 0:初始 1:完成.
     */
    private Integer status;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 额外信息.
     */
    private String extraInfo;
}
