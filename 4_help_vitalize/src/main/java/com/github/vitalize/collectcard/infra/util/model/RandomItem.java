package com.github.vitalize.collectcard.infra.util.model;

import lombok.Data;

/**
 * 进行随机的单位.
 *
 * @author yanghuan
 */
@Data
public class RandomItem {

    /**
     * ID.
     */
    private Long id;

    /**
     * 权重.
     */
    private long weight;
}
