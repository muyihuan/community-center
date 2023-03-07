package com.github.vitalize.collectcard.domains.usercard.repo.model;

import lombok.Data;

import java.util.Date;

/**
 * 用户卡牌.
 *
 * @author yanghuan
 */
@Data
public class UserCardDO {

    /**
     * 数据库ID.
     */
    private Long id;

    /**
     * 用户ID.
     */
    private String uid;

    /**
     * 卡组ID.
     */
    private Long cardGroupId;

    /**
     * 卡牌ID.
     */
    private Long cardId;

    /**
     * 卡牌数量.
     */
    private Long count;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 扩展信息.
     */
    private String extraInfo;
}
