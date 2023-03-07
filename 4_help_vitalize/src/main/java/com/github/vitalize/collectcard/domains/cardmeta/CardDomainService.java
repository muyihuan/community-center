package com.github.vitalize.collectcard.domains.cardmeta;

import com.github.vitalize.collectcard.domains.cardmeta.enums.CardBagLevelEnum;
import com.github.vitalize.collectcard.domains.cardmeta.enums.CardBagStateEnum;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardBagModel;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardBookModel;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardGroupModel;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardModel;
import com.github.vitalize.collectcard.domains.cardmeta.repo.CardBagRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.CardBookRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.CardGroupRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.CardRepository;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardBagDO;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardBookDO;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardDO;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardGroupDO;
import com.github.vitalize.collectcard.exception.CollectCardErrorCode;
import com.github.vitalize.collectcard.exception.CollectCardException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 卡牌元数据信息.
 *
 * @author yanghuan
 */
@Service
public class CardDomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardDomainService.class);

    @Autowired
    private CardBookRepository cardBookRepository;

    @Autowired
    private CardGroupRepository cardGroupRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardBagRepository cardBagRepository;

    /**
     * 获取卡牌信息.
     */
    public CardModel getCard(Long cardId) {
        if(cardId == null) {
            throw new CollectCardException(CollectCardErrorCode.PARAM_ERROR);
        }

        CardDO cardDO = cardRepository.getCard(cardId);
        if(cardDO == null) {
            return null;
        }

        return CardModel.transformCardDO(cardDO);
    }

    /**
     * 获取卡组信息.
     */
    public CardGroupModel getCardGroup(Long cardGroupId) {
        if(cardGroupId == null) {
            throw new CollectCardException(CollectCardErrorCode.PARAM_ERROR);
        }

        CardGroupDO cardGroup = cardGroupRepository.getCardBook(cardGroupId);
        if(cardGroup == null) {
            LOGGER.error("act=getCardGroup error cardGroup is null cardGroupId={}", cardGroupId);
            return null;
        }

        CardGroupModel cardGroupModel = CardGroupModel.transformCardDO(cardGroup);

        List<CardDO> cardDOList = cardRepository.getCardListByGroupId(cardGroup.getId());
        if(CollectionUtils.isNotEmpty(cardDOList)) {
            List<CardModel> cardModelList = cardDOList.stream().map(CardModel::transformCardDO).collect(Collectors.toList());
            cardGroupModel.setCardList(cardModelList);
        }

        return cardGroupModel;
    }

    /**
     * 获取卡册信息.
     */
    public CardBookModel getCardBook(String sceneKey) {
        Map<String, CardBookDO> cardBookDoMap = cardBookRepository.getCardBook();
        if(cardBookDoMap == null) {
            return null;
        }

        if(!cardBookDoMap.containsKey(sceneKey)){
            return null;
        }

        return assemblingCardBookModel(cardBookDoMap.get(sceneKey));
    }

    /**
     * 获取卡包信息.
     */
    public CardBagModel getCardTag(Long cardTagId) {
        if(cardTagId == null) {
            throw new CollectCardException(CollectCardErrorCode.PARAM_ERROR);
        }

        CardBagDO cardBagDO = cardBagRepository.getCardBag(cardTagId);
        if(cardBagDO == null) {
            return null;
        }

        CardBagModel cardBagModel = new CardBagModel();
        cardBagModel.setId(cardBagDO.getId());
        cardBagModel.setName(cardBagDO.getName());
        cardBagModel.setLevel(CardBagLevelEnum.getByCode(cardBagDO.getLevel()));
        cardBagModel.setStatus(CardBagStateEnum.NORMAL);
        cardBagModel.setBagCardsList(cardBagDO.getBagCardsList());
        return cardBagModel;
    }

    /**
     * 通过卡册id获取卡册信息.
     */
    public CardBookModel getCardBookById(Long id) {
        CardBookDO cardBookDO = cardBookRepository.getCardBookById(id);
        if(cardBookDO == null) {
            return null;
        }

        return assemblingCardBookModel(cardBookDO);
    }

    /**
     * 拼装 CardBookModel 信息.
     *
     * @param cardBookDO 卡册基本信息.
     * @return  CardBookModel.
     */
    private CardBookModel assemblingCardBookModel(CardBookDO cardBookDO){
        CardBookModel cardBookModel = new CardBookModel();
        cardBookModel.setId(cardBookDO.getId());
        cardBookModel.setName(cardBookDO.getName());

        List<Long> cardGroupList = cardBookDO.getCardGroupList();
        if(CollectionUtils.isNotEmpty(cardGroupList)) {
            List<CardGroupModel> groupModelList = new ArrayList<>(cardGroupList.size());
            for(Long groupId : cardGroupList) {
                groupModelList.add(getCardGroup(groupId));
            }
            cardBookModel.setCardGroupList(groupModelList);
        }
        return cardBookModel;
    }
}
