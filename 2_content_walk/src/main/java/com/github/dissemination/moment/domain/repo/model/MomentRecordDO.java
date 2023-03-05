package com.github.dissemination.moment.domain.repo.model;

import lombok.Data;

import java.util.Date;

/**
 * 朋友圈记录.
 *
 * @author yanghuan
 */
@Data
public class MomentRecordDO {

    /**
     * 谁的朋友圈.
     */
    private String uid;

    /**
     * 好友uid.
     */
    private String friendUid;

    /**
     * 好友发布的内容载体类型.
     */
    private Integer contentCarrierType;

    /**
     * 好友发布的内容ID.
     */
    private Long contentCarrierId;

    /**
     * 创建时间.
     */
    private Date createTime;
}
