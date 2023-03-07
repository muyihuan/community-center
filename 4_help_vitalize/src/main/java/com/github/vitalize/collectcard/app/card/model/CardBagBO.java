package com.github.vitalize.collectcard.app.card.model;

import com.github.vitalize.collectcard.domains.cardmeta.enums.CardBagLevelEnum;
import lombok.Data;

/**
 * 卡包信息.
 *
 * @author yanghuan
 */
@Data
public class CardBagBO {

    /**
     * 卡包的ID
     */
    private Long id;

    /**
     * 卡包的名称
     */
    private String name;

    /**
     * 卡包的等级
     */
    private CardBagLevelEnum level;
}
