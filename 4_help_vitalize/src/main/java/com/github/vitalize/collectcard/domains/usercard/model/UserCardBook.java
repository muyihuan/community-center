package com.github.vitalize.collectcard.domains.usercard.model;

import lombok.Data;

import java.util.List;

/**
 * 用户卡册信息.
 *
 * @author yanghuan
 */
@Data
public class UserCardBook {

    /**
     * 用户ID.
     */
    private String uid;

    /**
     * 册内卡组及顺序.
     */
    private List<CardGroup> cardGroupList;
}
