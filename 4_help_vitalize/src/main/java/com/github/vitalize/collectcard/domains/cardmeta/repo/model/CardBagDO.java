package com.github.vitalize.collectcard.domains.cardmeta.repo.model;

import com.github.vitalize.collectcard.domains.cardmeta.model.BagInfo;
import lombok.Data;

import java.util.List;

/**
 * 卡包存储元数据.
 *
 * @author yanghuan
 */
@Data
public class CardBagDO {

    /**
     * 卡包的ID.
     */
    private Long id;

    /**
     * 卡包的名称.
     */
    private String name;

    /**
     * 卡包的等级.
     */
    private Integer level;

    /**
     * 包内卡牌和卡牌权重.
     */
    private List<List<BagInfo>> bagCardsList;
}
