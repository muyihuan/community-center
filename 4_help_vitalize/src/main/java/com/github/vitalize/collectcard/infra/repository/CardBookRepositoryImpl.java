package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.domains.cardmeta.repo.CardBookRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardBookDO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 卡册模版存储实现.
 *
 * @author yanghuan
 */
@Service
public class CardBookRepositoryImpl implements CardBookRepository {

    @Override
    public Map<String, CardBookDO> getCardBook() {
        return null;
    }

    @Override
    public CardBookDO getCardBookById(Long id) {
        return null;
    }
}
