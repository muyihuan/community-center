package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.domains.cardmeta.repo.CardBagRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardBagDO;
import org.springframework.stereotype.Service;

/**
 * 卡包存储实现.
 *
 * @author yanghuan
 */
@Service
public class CardBagRepositoryImpl implements CardBagRepository {

    @Override
    public CardBagDO getCardBag(Long id) {
        return null;
    }
}
