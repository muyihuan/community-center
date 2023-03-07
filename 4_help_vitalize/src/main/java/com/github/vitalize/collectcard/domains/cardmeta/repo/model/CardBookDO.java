package com.github.vitalize.collectcard.domains.cardmeta.repo.model;

import lombok.Data;

import java.util.List;

/**
 * 卡册存储元数据.
 *
 * @author yanghuan
 */
@Data
public class CardBookDO {

    /**
     * 卡册的ID.
     */
    private Long id;

    /**
     * 卡册的名称.
     */
    private String name;

    /**
     * 册内卡组及顺序.
     */
    private List<Long> cardGroupList;
}
