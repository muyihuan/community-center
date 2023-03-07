package com.github.vitalize.collectcard.domains.cardmeta.repo.model;

import lombok.Data;

import java.util.Date;

/**
 * 卡组存储元数据.
 *
 * @author yanghuan
 */
@Data
public class CardGroupDO {

    /**
     * 卡组的ID.
     */
    private Long id;

    /**
     * 卡组的名称.
     */
    private String name;

    /**
     * 封面图.
     */
    private String coverImg;

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
}
