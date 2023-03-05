package com.github.dissemination.mine.domain.repo.model;

import lombok.Data;

import java.util.Date;

/**
 * 个人动态记录.
 *
 * @author yanghuan
 */
@Data
public class MineContentDO {

    /**
     * 谁的个人页.
     */
    private String uid;

    /**
     * 内容载体类型.
     */
    private Integer contentCarrierType;

    /**
     * 载体ID.
     */
    private Long contentCarrierId;

    /**
     * 动态的可见性.
     */
    private Integer privilege;

    /**
     * 创建时间.
     */
    private Date createTime;
}
