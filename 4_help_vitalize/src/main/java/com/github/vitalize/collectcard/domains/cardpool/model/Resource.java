package com.github.vitalize.collectcard.domains.cardpool.model;

import lombok.Data;

/**
 * 资源概率配置.
 *
 * @author yanghuan
 */
@Data
public class Resource {

    /**
     * 资源类型 0：卡牌 1：卡包.
     */
    private Integer resourceType;

    /**
     * 资源ID.
     */
    private Long id;

    /**
     * 权重.
     */
    private Long weight;
}
