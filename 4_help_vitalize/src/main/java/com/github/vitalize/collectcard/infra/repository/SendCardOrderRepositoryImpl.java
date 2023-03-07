package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.app.card.repo.SendCardOrderRepository;
import com.github.vitalize.collectcard.app.card.repo.model.SendCardOrderDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 赠卡运送订单存储实现.
 *
 * @author yanghuan
 */
@Service
public class SendCardOrderRepositoryImpl implements SendCardOrderRepository {

    @Override
    public Long createOrder(SendCardOrderDO sendCardOrderDO) {
        return null;
    }

    @Override
    public boolean delOrder(Long orderId) {
        return false;
    }

    @Override
    public SendCardOrderDO getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<SendCardOrderDO> queryExpireOrderList(Integer count) {
        return null;
    }
}
