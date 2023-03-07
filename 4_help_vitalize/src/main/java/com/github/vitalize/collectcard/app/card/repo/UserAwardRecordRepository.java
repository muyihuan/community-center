package com.github.vitalize.collectcard.app.card.repo;

import com.github.vitalize.collectcard.app.card.repo.model.AwardRecordDO;

import java.util.List;

/**
 * 奖励记录存储.
 *
 * @author yanghuan
 */
public interface UserAwardRecordRepository {

    /**
     * 创建奖励记录.
     *
     * @param awardRecordDO 奖励信息
     * @return
     */
    Long createRecord(AwardRecordDO awardRecordDO);

    /**
     * 下发完成.
     *
     * @param recordId 奖励记录ID
     * @param orderId 对应下发的订单ID
     * @return
     */
    boolean finishAwardRecordById(Long recordId, String orderId);

    /**
     * 获取用户奖励记录.
     *
     * @param uid 用户
     * @return
     */
    List<AwardRecordDO> queryUserAwardRecordList(String uid);
}
