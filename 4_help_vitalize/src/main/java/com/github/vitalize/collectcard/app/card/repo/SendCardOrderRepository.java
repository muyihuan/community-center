package com.github.vitalize.collectcard.app.card.repo;

import com.github.vitalize.collectcard.app.card.repo.model.SendCardOrderDO;

import java.util.List;

/**
 * 赠卡运送订单存储.
 *
 * @author yanghuan
 */
public interface SendCardOrderRepository {

    /**
     * 创建订单.
     *
     * @param sendCardOrderDO
     * @return
     */
    Long createOrder(SendCardOrderDO sendCardOrderDO);

    /**
     * 删除订单.
     *
     * @param orderId
     * @return
     */
    boolean delOrder(Long orderId);

    /**
     * 通过Id获取订单.
     *
     * @param orderId
     * @return
     */
    SendCardOrderDO getOrderById(Long orderId);

    /**
     * 获取过期的订单.
     *
     * @param count
     * @return
     */
    List<SendCardOrderDO> queryExpireOrderList(Integer count);
}
