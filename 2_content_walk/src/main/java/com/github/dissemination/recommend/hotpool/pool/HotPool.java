package com.github.dissemination.recommend.hotpool.pool;

import com.github.dissemination.recommend.hotpool.pool.model.PoolEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * 基于内容热度的池子.
 *
 * @author yanghuan
 */
public class HotPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotPool.class);

    private Long poolId;

    public Long getId() {
        return poolId;
    }

    public List<PoolEntity> queryPoolEntities(Integer offset, Integer count) {
        return Collections.emptyList();
    }

    public void enterPool(List<PoolEntity> entities) {

    }

    public void leavePool(List<PoolEntity> entities) {

    }

    public Long nextPool() {
        return 0L;
    }

    public void destroy() {

    }
}
