package com.github.vitalize.collectcard.domains.usercard.repo;

import com.github.vitalize.collectcard.domains.usercard.repo.model.UserCardDO;

import java.util.List;

/**
 * 用户卡册存储.
 *
 * @author yanghuan
 */
public interface UserCardBookRepository {

    /**
     * 保存卡牌.
     *
     * @param uid
     * @param cardId
     * @param cardGroupId
     * @param extraInfo
     */
    void saveCard(String uid, Long cardId, Long cardGroupId, String extraInfo);

    /**
     * 更新卡牌扩展信息.
     *
     * @param uid
     * @param cardId
     * @param extraInfo
     * @return
     */
    boolean updateCardExtraInfo(String uid, Long cardId, String extraInfo);

    /**
     * 增加卡牌数量.
     *
     * @param uid
     * @param cardId
     */
    void incrCardCount(String uid, Long cardId);

    /**
     * 减少卡牌数量.
     *
     * @param uid
     * @param cardId
     * @return
     */
    boolean decrCardCount(String uid, Long cardId);

    /**
     * 删除卡牌.
     *
     * @param uid
     * @param cardId
     * @return
     */
    boolean delCard(String uid, Long cardId);

    /**
     * 查询用户某个卡牌.
     *
     * @param uid
     * @param cardId
     * @return
     */
    UserCardDO queryUserCard(String uid, Long cardId);

    /**
     * 查询用户卡牌.
     *
     * @param uid
     * @return
     */
    List<UserCardDO> queryUserAllCards(String uid);

    /**
     * 查询用户卡组卡牌.
     *
     * @param uid
     * @param cardGroupId
     * @return
     */
    List<UserCardDO> queryUserCardGroupCardList(String uid, Long cardGroupId);
}
