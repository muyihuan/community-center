package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.domains.usercard.repo.UserCardBookRepository;
import com.github.vitalize.collectcard.domains.usercard.repo.model.UserCardDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户卡册存储实现.
 *
 * @author yanghuan
 */
@Service
public class UserCardRepositoryImpl implements UserCardBookRepository {

    @Override
    public void saveCard(String uid, Long cardId, Long cardGroupId, String extraInfo) {

    }

    @Override
    public boolean updateCardExtraInfo(String uid, Long cardId, String extraInfo) {
        return false;
    }

    @Override
    public void incrCardCount(String uid, Long cardId) {

    }

    @Override
    public boolean decrCardCount(String uid, Long cardId) {
        return false;
    }

    @Override
    public boolean delCard(String uid, Long cardId) {
        return false;
    }

    @Override
    public UserCardDO queryUserCard(String uid, Long cardId) {
        return null;
    }

    @Override
    public List<UserCardDO> queryUserAllCards(String uid) {
        return null;
    }

    @Override
    public List<UserCardDO> queryUserCardGroupCardList(String uid, Long cardGroupId) {
        return null;
    }
}
