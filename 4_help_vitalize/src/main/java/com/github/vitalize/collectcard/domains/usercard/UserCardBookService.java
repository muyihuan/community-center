package com.github.vitalize.collectcard.domains.usercard;

import com.github.infrastructure.json.JsonMapper;
import com.github.vitalize.collectcard.domains.cardmeta.CardDomainService;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardBookModel;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardGroupModel;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardModel;
import com.github.vitalize.collectcard.domains.usercard.model.Card;
import com.github.vitalize.collectcard.domains.usercard.model.CardGroup;
import com.github.vitalize.collectcard.domains.usercard.model.UserCardBook;
import com.github.vitalize.collectcard.domains.usercard.repo.UserCardBookRepository;
import com.github.vitalize.collectcard.domains.usercard.repo.model.ExtraInfo;
import com.github.vitalize.collectcard.domains.usercard.repo.model.UserCardDO;
import com.github.vitalize.collectcard.exception.CollectCardErrorCode;
import com.github.vitalize.collectcard.exception.CollectCardException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户卡册模块.
 *
 * @author yanghuan
 */
@Service
public class UserCardBookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardDomainService.class);

    @Autowired
    private UserCardBookRepository userCardBookRepository;

    @Autowired
    private CardDomainService cardDomainService;

    private static final String KEY_COLLECT_CARD_USER_BOOK_LOCK = "c:c:user:card:book:lock";

    /**
     * 获取用户卡册卡牌.
     */
    public UserCardBook getUserCardBook(String uid, String sceneKey) {
        UserCardBook userCardBook = new UserCardBook();
        userCardBook.setUid(uid);

        List<UserCardDO> cardDOList = userCardBookRepository.queryUserAllCards(uid);
        if(CollectionUtils.isEmpty(cardDOList)) {
            return userCardBook;
        }

        CardBookModel cardBookTemplate = cardDomainService.getCardBook(sceneKey);
        if(cardBookTemplate == null) {
            return userCardBook;
        }

        List<Long> cardGroupIdsInTemplate = cardBookTemplate.getCardGroupList().stream().map(CardGroupModel::getId).collect(Collectors.toList());
        Map<Long, List<UserCardDO>> groupCardsMap = cardDOList.stream()
                .filter(card -> cardGroupIdsInTemplate.contains(card.getCardGroupId()))
                .collect(Collectors.groupingBy(UserCardDO::getCardGroupId));
        if(groupCardsMap.isEmpty()) {
            return userCardBook;
        }

        List<CardGroup> cardGroupList = new ArrayList<>();
        for(Long groupId : groupCardsMap.keySet()) {
            CardGroup cardGroup = new CardGroup();
            cardGroup.setId(groupId);

            List<UserCardDO> cards = groupCardsMap.get(groupId);
            List<Card> cardList = new ArrayList<>(cards.size());
            for(UserCardDO userCardDO : cards) {
                Card card = new Card();
                card.setId(userCardDO.getCardId());
                card.setCount(userCardDO.getCount());
                if(StringUtils.isNotEmpty(userCardDO.getExtraInfo())) {
                    ExtraInfo extraInfo = JsonMapper.fromJson(userCardDO.getExtraInfo(), ExtraInfo.class);
                    Card.ExtraInfo cardExtraInfo = new Card.ExtraInfo();
                    cardExtraInfo.setLock(extraInfo.getIsLock());
                    card.setExtraInfo(cardExtraInfo);

                    if(BooleanUtils.isTrue(extraInfo.getIsCollectCompleted())) {
                        CardGroup.ExtraInfo groupExtraInfo = new CardGroup.ExtraInfo();
                        groupExtraInfo.setCollectCompleted(true);
                        cardGroup.setExtraInfo(groupExtraInfo);
                    }
                }
                cardList.add(card);
            }

            cardGroup.setCardList(cardList);
            cardGroupList.add(cardGroup);
        }
        userCardBook.setCardGroupList(cardGroupList);

        return userCardBook;
    }

    /**
     * 获取用户卡册的卡组数据.
     */
    public CardGroup getUserCardGroup(String uid, Long cardGroupId) {
        CardGroup cardGroup = new CardGroup();
        cardGroup.setId(cardGroupId);

        List<UserCardDO> cardDOList = userCardBookRepository.queryUserCardGroupCardList(uid, cardGroupId);
        if(CollectionUtils.isEmpty(cardDOList)) {
            cardGroup.setCardList(Collections.emptyList());
            return cardGroup;
        }

        boolean isGroupCompleted = false;
        List<Card> cardList = new ArrayList<>(cardDOList.size());
        for(UserCardDO userCardDO : cardDOList) {
            Card card = new Card();
            card.setId(userCardDO.getCardId());
            card.setCount(userCardDO.getCount());
            if(StringUtils.isNotEmpty(userCardDO.getExtraInfo())) {
                ExtraInfo extraInfo = JsonMapper.fromJson(userCardDO.getExtraInfo(), ExtraInfo.class);
                Card.ExtraInfo cardExtraInfo = new Card.ExtraInfo();
                cardExtraInfo.setLock(extraInfo.getIsLock());
                card.setExtraInfo(cardExtraInfo);

                // 卡组是否收集且卡牌全部激活了
                if(BooleanUtils.isTrue(extraInfo.getIsCollectCompleted())) {
                    isGroupCompleted = true;
                }
            }
            cardList.add(card);
        }

        cardGroup.setCardList(cardList);
        CardGroup.ExtraInfo groupExt = new CardGroup.ExtraInfo();
        groupExt.setCollectCompleted(isGroupCompleted);
        cardGroup.setExtraInfo(groupExt);
        return cardGroup;
    }

    /**
     * 查询用户卡片.
     */
    public Card queryUserCard(String uid, Long cardId) {
        UserCardDO userCardDO = userCardBookRepository.queryUserCard(uid, cardId);
        if(userCardDO == null) {
            return null;
        }

        Card card = new Card();
        card.setId(userCardDO.getCardId());
        card.setCount(userCardDO.getCount());
        if(StringUtils.isNotEmpty(userCardDO.getExtraInfo())) {
            ExtraInfo extraInfo = JsonMapper.fromJson(userCardDO.getExtraInfo(), ExtraInfo.class);
            if(BooleanUtils.isTrue(extraInfo.getIsLock())) {
                Card.ExtraInfo extInfo = new Card.ExtraInfo();
                extInfo.setLock(true);
                card.setExtraInfo(extInfo);
            }
        }

        return card;
    }

    /**
     * 将卡牌放入用户卡册.
     */
    public void putIntoCard(String uid, long cardId) {
        CardModel cardModel = cardDomainService.getCard(cardId);
        if(cardModel == null) {
            throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
        }

        // lock
        try {
            // 如果卡牌已拥有，卡牌数量加1
            UserCardDO userCardDO = userCardBookRepository.queryUserCard(uid, cardId);
            if(userCardDO != null) {
                userCardBookRepository.incrCardCount(uid, cardId);
            }
            else {
                userCardBookRepository.saveCard(uid, cardId, cardModel.getCardGroupId(), "");
            }
        }
        finally {
            // unlock
        }
    }

    /**
     * 从用户卡册取出某个卡牌.
     *
     * @return true 成功 false 失败
     */
    public boolean takeOutCard(String uid, Long cardId, List<Long> cardGroup) {
        CardModel cardModel = cardDomainService.getCard(cardId);
        // 如果卡牌信息不存在
        if(cardModel == null) {
            throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
        }

        // 当前场景中活动卡组信息与卡牌分组信息不一致，则数据有误，取出卡牌时的安全感性,为了兼容之前，没有传场景key，不限制
        if(CollectionUtils.isNotEmpty(cardGroup) && !cardGroup.contains(cardModel.getCardGroupId())) {
            throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
        }

        // lock
        try {
            UserCardDO userCardDO = userCardBookRepository.queryUserCard(uid, cardId);
            if(userCardDO == null) {
                // 没有卡牌，取出失败
                return false;
            }

            boolean res;
            if(userCardDO.getCount() == 1) {
                if(StringUtils.isNotEmpty(userCardDO.getExtraInfo())) {
                    ExtraInfo extraInfo = JsonMapper.fromJson(userCardDO.getExtraInfo(), ExtraInfo.class);
                    if(BooleanUtils.isTrue(extraInfo.getIsLock())) {
                        // 如果用户卡牌只剩一张，且卡牌已经被锁定，那么取出失败
                        return false;
                    }
                }

                res = userCardBookRepository.delCard(uid, cardId);
            }
            else {
                res = userCardBookRepository.decrCardCount(uid, cardId);
            }

            return res;
        }
        finally {
            // unlock
        }
    }

    /**
     * 锁定卡牌，不允许被赠送等操作.
     */
    public boolean lockCard(String uid, Long cardId) {
        CardModel cardModel = cardDomainService.getCard(cardId);
        if(cardModel == null) {
            throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
        }

        // lock
        try {
            UserCardDO userCardDO = userCardBookRepository.queryUserCard(uid, cardId);
            if(userCardDO == null) {
                // 没有卡牌，锁定失败
                return false;
            }

            if(StringUtils.isNotEmpty(userCardDO.getExtraInfo())) {
                ExtraInfo extraInfo = JsonMapper.fromJson(userCardDO.getExtraInfo(), ExtraInfo.class);
                if(BooleanUtils.isTrue(extraInfo.getIsLock())) {
                    // 用户已经锁定了，锁定失败
                    return false;
                }
            }

            ExtraInfo extraInfo = new ExtraInfo();
            extraInfo.setIsLock(true);
            return userCardBookRepository.updateCardExtraInfo(uid, cardId, JsonMapper.toJson(extraInfo));
        }
        finally {
            // unlock
        }
    }

    /**
     * 更新卡组的扩展信息.
     */
    public boolean updateUserCardGroupExtraInfo(String uid, Long cardGroupId, Long extCardId, Boolean extIsCompleted) {
        // lock
        try {
            UserCardDO userCardDO = userCardBookRepository.queryUserCard(uid, extCardId);
            if(userCardDO == null || !userCardDO.getCardGroupId().equals(cardGroupId)) {
                throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
            }

            ExtraInfo extraInfo = new ExtraInfo();
            if(StringUtils.isNotEmpty(userCardDO.getExtraInfo())) {
                extraInfo = JsonMapper.fromJson(userCardDO.getExtraInfo(), ExtraInfo.class);
            }

            extraInfo.setIsCollectCompleted(extIsCompleted);
            return userCardBookRepository.updateCardExtraInfo(uid, extCardId, JsonMapper.toJson(extraInfo));
        }
        finally {
            // unlock
        }
    }
}
