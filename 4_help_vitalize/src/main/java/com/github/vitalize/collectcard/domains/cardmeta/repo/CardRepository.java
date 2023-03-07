package com.github.vitalize.collectcard.domains.cardmeta.repo;

import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardDO;

import java.util.List;

/**
 * 卡牌存储.
 *
 * @author yanghuan
 */
public interface CardRepository {

    /**
     * 通过ID查询卡牌.
     *
     * @param id 卡牌ID.
     * @return 卡牌元数据.
     */
    CardDO getCard(Long id);

    /**
     * 获取卡组内卡牌.
     *
     * @param groupId 卡组ID.
     * @return 卡组内卡牌列表.
     */
    List<CardDO> getCardListByGroupId(Long groupId);
}
