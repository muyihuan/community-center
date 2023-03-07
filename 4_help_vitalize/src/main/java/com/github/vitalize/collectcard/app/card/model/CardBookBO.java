package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

import java.util.List;

/**
 * 卡册实体.
 *
 * @author yanghuan
 */
@Data
public class CardBookBO {

    /**
     * 卡册的所有人.
     */
    private String uid;

    /**
     * 册内卡组及顺序.
     */
    private List<CardGroupBO> cardGroupList;
}
