package com.github.vitalize.collectcard.domains.cardmeta.model;

import lombok.Data;

/**
 * 扩展信息.
 *
 * @author yanghuan
 */
@Data
public class BagInfo {

    /**
     * 卡牌信息.
     */
    private Long cardId;

    /**
     * 卡牌信息.
     */
    private Integer cardLevel;

    /**
     * 权重.
     */
    private Integer weight;
}
