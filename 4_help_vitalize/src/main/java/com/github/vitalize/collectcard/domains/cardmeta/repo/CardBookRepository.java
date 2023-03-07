package com.github.vitalize.collectcard.domains.cardmeta.repo;

import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardBookDO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 卡册模版存储.
 *
 * @author yanghuan
 */
@Service
public interface CardBookRepository {

    /**
     * 通过ID查询卡包.
     *
     * @return 卡册元数据.
     */
    Map<String, CardBookDO> getCardBook();

    /**
     * 通过ID查询卡包.
     *
     * @return 卡册元数据.
     */
    CardBookDO getCardBookById(Long id);
}
