package com.github.vitalize.collectcard.domains.cardmeta.model;

import lombok.Data;

import java.util.List;

/**
 * 卡册实体.
 *
 * @author yanghuan
 */
@Data
public class CardBookModel {

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
    private List<CardGroupModel> cardGroupList;
}
