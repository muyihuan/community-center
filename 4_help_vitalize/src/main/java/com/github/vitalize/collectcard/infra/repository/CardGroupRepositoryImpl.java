package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.domains.cardmeta.repo.CardGroupRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardGroupDO;
import org.springframework.stereotype.Service;

/**
 * 卡组实体存储实现.
 *
 * @author yanghuan
 */
@Service
public class CardGroupRepositoryImpl implements CardGroupRepository {

    @Override
    public CardGroupDO getCardBook(Long id) {
        return null;
    }
}
