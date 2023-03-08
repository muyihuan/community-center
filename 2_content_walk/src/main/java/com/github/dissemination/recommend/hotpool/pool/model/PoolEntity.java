package com.github.dissemination.recommend.hotpool.pool.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 池子内容单元.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class PoolEntity {

    /**
     * 内容的唯一标识.
     */
    private String id;

    /**
     * 内容的热度分值.
     */
    private Double score;
}
