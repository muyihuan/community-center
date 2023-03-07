package com.github.vitalize.collectcard.domains.cardmeta.repo.model;

import lombok.Data;

import java.util.Date;

/**
 * 卡牌存储元数据.
 *
 * @author yanghuan
 */
@Data
public class CardDO {

    /**
     * 卡牌的ID.
     */
    private Long id;

    /**
     * 卡牌的名称.
     */
    private String name;

    /**
     * 卡牌的等级.
     */
    private Integer level;

    /**
     * 封面图.
     */
    private String coverImg;

    /**
     * 所属卡组的ID.
     */
    private Long cardGroupId;

    /**
     * 状态.
     */
    private Integer status;

    /**
     * 排序位置.
     */
    private Integer sortIdx;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 扩展信息.
     */
    private String extraInfo;
}
