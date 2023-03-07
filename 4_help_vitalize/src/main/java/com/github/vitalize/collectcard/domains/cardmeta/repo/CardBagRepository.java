package com.github.vitalize.collectcard.domains.cardmeta.repo;

import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardBagDO;

/**
 * 卡包存储.
 *
 * @author yanghuan
 */
public interface CardBagRepository {

    /**
     * 通过ID查询卡包.
     *
     * @param id 卡包ID.
     * @return 卡包信息.
     */
    CardBagDO getCardBag(Long id);
}
