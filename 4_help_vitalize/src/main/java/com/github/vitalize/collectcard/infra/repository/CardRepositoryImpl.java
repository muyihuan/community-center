package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.domains.cardmeta.repo.CardRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卡包存储实现.
 *
 * @author yanghuan
 */
@Service
public class CardRepositoryImpl implements CardRepository {

    @Override
    public CardDO getCard(Long id) {
        return null;
    }

    @Override
    public List<CardDO> getCardListByGroupId(Long groupId) {
        return null;
    }
}
