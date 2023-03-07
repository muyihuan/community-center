package com.github.vitalize.collectcard.domains.cardmeta.repo;

import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardGroupDO;
import org.springframework.stereotype.Service;

/**
 * 卡组实体存储.
 *
 * @author yanghuan
 */
@Service
public interface CardGroupRepository {

    /**
     * 通过ID查询卡组.
     *
     * @param id 卡组ID.
     * @return 卡组元数据.
     */
    CardGroupDO getCardBook(Long id);
}
