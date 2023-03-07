package com.github.vitalize.collectcard.app.card.model;

import com.github.vitalize.collectcard.domains.cardmeta.enums.CardLevelEnum;
import lombok.Data;

/**
 * 卡牌.
 *
 * @author yanghuan
 */
@Data
public class CardBO {

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
    private CardLevelEnum level;

    /**
     * 封面图.
     */
    private String coverImg;

    /**
     * 所属卡组的ID.
     */
    private Long cardGroupId;

    /**
     * 卡牌数量.
     */
    private Long count;

    /**
     * 是否锁定.
     */
    private Boolean isLock;
}
