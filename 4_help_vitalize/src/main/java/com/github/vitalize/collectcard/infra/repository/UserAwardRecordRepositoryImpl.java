package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.app.card.repo.UserAwardRecordRepository;
import com.github.vitalize.collectcard.app.card.repo.model.AwardRecordDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 奖励记录存储实现.
 *
 * @author yanghuan
 */
@Service
public class UserAwardRecordRepositoryImpl implements UserAwardRecordRepository {


    @Override
    public Long createRecord(AwardRecordDO awardRecordDO) {
        return null;
    }

    @Override
    public boolean finishAwardRecordById(Long recordId, String orderId) {
        return false;
    }

    @Override
    public List<AwardRecordDO> queryUserAwardRecordList(String uid) {
        return null;
    }
}
