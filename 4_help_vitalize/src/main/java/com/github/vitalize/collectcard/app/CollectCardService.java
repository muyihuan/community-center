package com.github.vitalize.collectcard.app;

import com.github.infrastructure.json.JsonMapper;
import com.github.vitalize.collectcard.app.card.LimitService;
import com.github.vitalize.collectcard.app.card.SendCardOrderService;
import com.github.vitalize.collectcard.app.card.config.LimitConfig;
import com.github.vitalize.collectcard.app.card.model.*;
import com.github.vitalize.collectcard.app.scene.SceneService;
import com.github.vitalize.collectcard.app.scene.model.SceneModel;
import com.github.vitalize.collectcard.domains.cardmeta.CardDomainService;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardBagModel;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardGroupModel;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardModel;
import com.github.vitalize.collectcard.domains.cardpool.CardResourceService;
import com.github.vitalize.collectcard.domains.usercard.UserCardBookService;
import com.github.vitalize.collectcard.domains.usercard.model.Card;
import com.github.vitalize.collectcard.domains.usercard.model.CardGroup;
import com.github.vitalize.collectcard.domains.usercard.model.UserCardBook;
import com.github.vitalize.collectcard.exception.CollectCardErrorCode;
import com.github.vitalize.collectcard.exception.CollectCardException;
import com.github.vitalize.collectcard.infra.rpc.ChatMsgFacade;
import com.github.vitalize.collectcard.infra.rpc.FriendFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 集卡业务服务.
 *
 * @author yanghuan
 */
@Service
public class CollectCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectCardService.class);

    @Autowired
    private CardDomainService cardDomainService;

    @Autowired
    private UserCardBookService userCardBookService;

    @Autowired
    private CardResourceService cardResourceService;

    @Autowired
    private SendCardOrderService sendCardOrderService;

    @Autowired
    private LimitService limitService;

    @Autowired
    private ChatMsgFacade chatMsgFacade;

    @Autowired
    private FriendFacade friendFacade;

    @Autowired
    private SceneService sceneService;

    private static final String KEY_COLLECT_CARD_ISSUED_LOCK = "c:c:issued:user:lock:";
    private static final String KEY_COLLECT_CARD_MODIFY_LOCK = "c:c:modify:user:lock:";

    /**
     * 开/抽/下发 卡.
     */
    public IssuedCardResult issuedCards(String uid, String sceneKey) {
        UserCardBook userCardBook = userCardBookService.getUserCardBook(uid, sceneKey);
        if(userCardBook == null) {
            return null;
        }

        // lock
        try {
            // 1.每天获取卡牌上限 0 为不限制.
            int dayIssuedCardCountMax = LimitConfig.get(sceneKey).getDayIssuedCardCountMax();
            if(dayIssuedCardCountMax != 0 && limitService.getUserTodayIssuedSuccCount(uid) >= dayIssuedCardCountMax) {
                LOGGER.info("act=issuedCards fail max limit uid={}", uid);
                return null;
            }

            // 2.从资源池获取卡牌.
            CardBagModel cardBagModel = cardResourceService.getCardBag(sceneKey);
            if(cardBagModel == null) {
                LOGGER.info("act=issuedCards fail cardBagModel is null uid={}", uid);
                return null;
            }

            // 3.随机一张卡牌.
            List<Long> cardIdList = cardBagModel.randomCardByWeight();
            if(CollectionUtils.isEmpty(cardIdList)) {
                LOGGER.info("act=issuedCards fail cardIdList isEmpty uid={}", uid);
                return null;
            }

            // 4.放入用户卡册.
            List<CardModel> cardModelList = new ArrayList<>();
            cardIdList.forEach((cardId) -> {
                userCardBookService.putIntoCard(uid, cardId);
                cardModelList.add(cardDomainService.getCard(cardId));
            });

            // 5.增加用户今日成功获取卡的次数.
            limitService.incrUserTodayIssuedSuccCount(uid);

            IssuedCardResult result = new IssuedCardResult();
            result.setCardBagId(cardBagModel.getId());
            result.setCardList(cardModelList);
            return result;
        }
        finally {
            // unlock
        }
    }

    /**
     * 查询用户卡册数据.
     */
    public UserCardBook getUserCardBook(String uid, String sceneKey) {
        return userCardBookService.getUserCardBook(uid, sceneKey);
    }

    /**
     * 赠卡.
     */
    public SendCardResultBO sendCard(String senderUid, String receiverUid, Long cardId, String sceneKey) {
        SendCardResultBO result = new SendCardResultBO();

        boolean isFriend = friendFacade.isFriend(senderUid, receiverUid);
        if(!isFriend) {
            result.setIsSucceed(false);
            result.setFailMsg("对方不是您的好友");
            return result;
        }

        Long orderId = null;
        // lock
        try {
            if(limitService.getUserTodaySendCardCount(senderUid) >= LimitConfig.get(sceneKey).getDaySendCardCountMax()) {
                result.setIsSucceed(false);
                result.setFailMsg("今日已达赠送上限，明天再送TA吧～");
                return result;
            }

            Card userCard = userCardBookService.queryUserCard(senderUid, cardId);
            if(userCard == null || userCard.getCount() <= 1) {
                result.setIsSucceed(false);
                result.setFailMsg("没有可送卡牌");
                return result;
            }
            List<Long> cardGroup = new ArrayList<>();
            if(StringUtils.isNotEmpty(sceneKey)){
                SceneModel sceneModel = sceneService.getSceneModelByKey(sceneKey);
                if(sceneModel == null){
                    result.setIsSucceed(false);
                    result.setFailMsg("活动已结束");
                    return result;
                }
                cardGroup = sceneModel.getCardGroup();
                if(CollectionUtils.isEmpty(cardGroup)){
                    result.setIsSucceed(false);
                    result.setFailMsg("活动已结束");
                    return result;
                }
            }
            boolean res = userCardBookService.takeOutCard(senderUid, cardId, cardGroup);
            if(!res) {
                result.setIsSucceed(false);
                result.setFailMsg("没有可送卡牌");
                return result;
            }

            orderId = sendCardOrderService.createOrder(senderUid, receiverUid, cardId, LimitConfig.get(sceneKey).getSendCardExpireHours());
            if(orderId == null) {
                // 创建订单失败，归还卡牌.
                userCardBookService.putIntoCard(senderUid, cardId);
                result.setIsSucceed(false);
                result.setFailMsg("订单生成失败");
                return result;
            }

            limitService.incrUserTodaySendCardCount(senderUid);
        }
        catch (Exception e) {
            // 创建订单发生异常，归还卡牌.
            userCardBookService.putIntoCard(senderUid, cardId);
            LOGGER.error("act=sendCard error senderUid={} receiverUid={} cardId={}", senderUid, receiverUid, cardId);
            result.setIsSucceed(false);
            result.setFailMsg("服务异常");
            return result;
        }
        finally {
            // unlock
        }

        CardModel cardModel = cardDomainService.getCard(cardId);
        // 发送消息.
        chatMsgFacade.sendSendCardCommonMsgCard(senderUid, receiverUid, receiverUid, orderId, TimeUnit.HOURS.toMillis(LimitConfig.get(sceneKey).getSendCardExpireHours()), cardModel.getSmallCoverImg(),sceneKey);
        chatMsgFacade.sendSendCardCommonMsgCardNoButton(senderUid, receiverUid, senderUid, orderId, TimeUnit.HOURS.toMillis(LimitConfig.get(sceneKey).getSendCardExpireHours()), cardModel.getSmallCoverImg());

        result.setIsSucceed(true);
        return result;
    }

    /**
     * 收卡.
     */
    public ReceiveCardResultBO receiveCard(String uid, Long sendOrderId, String sceneKey) {
        ReceiveCardResultBO result = new ReceiveCardResultBO();

        SendCardOrderModel orderModel = sendCardOrderService.getOrderById(sendOrderId);
        if(orderModel == null) {
            result.setIsSucceed(false);
            result.setFailMsg("已过期，无法领取卡片");
            return result;
        }

        if(!StringUtils.equals(orderModel.getReceiverUid(), uid)) {
            result.setIsSucceed(false);
            result.setFailMsg("您无法操作");
            return result;
        }

        // lock
        try {
            if(limitService.getUserTodayReceiveCardCount(uid) >= LimitConfig.get(sceneKey).getDayReceiveCardCountMax()) {
                result.setIsSucceed(false);
                result.setFailMsg("今日已达收卡上限，明天再求助TA吧～");
                return result;
            }

            boolean res = sendCardOrderService.finishOrderById(sendOrderId);
            if(!res) {
                result.setIsSucceed(false);
                result.setFailMsg("请勿重复处理");
                return result;
            }

            userCardBookService.putIntoCard(uid, orderModel.getCardId());

            limitService.incrUserTodayReceiveCardCount(uid);

            chatMsgFacade.sendChatMsg(uid, orderModel.getSenderUid(), "非常感谢你赠送的卡牌，正是我需要的，你需要什么卡牌也可以跟我说哦～");

            LOGGER.info("act=receiveCard receiverUid={} orderModel={}", uid, JsonMapper.toJson(orderModel));
            result.setIsSucceed(true);
            return result;
        }
        catch (Exception e) {
            LOGGER.error("act=receiveCard error receiverUid={} orderModel={}", uid, JsonMapper.toJson(orderModel));
            result.setIsSucceed(false);
            result.setFailMsg("服务异常");
            return result;
        }
        finally {
            // unlock
        }
    }

    /**
     * 拒绝收卡.
     */
    public RejectReceiveCardResultBO rejectReceiveCard(String uid, Long sendOrderId) {
        RejectReceiveCardResultBO result = new RejectReceiveCardResultBO();

        SendCardOrderModel orderModel = sendCardOrderService.getOrderById(sendOrderId);
        if(orderModel == null) {
            result.setIsSucceed(false);
            result.setFailMsg("已过期，卡牌已退回");
            return result;
        }

        if(!StringUtils.equals(uid, orderModel.getReceiverUid())) {
            result.setIsSucceed(false);
            result.setFailMsg("您无法操作");
            return result;
        }

        // lock
        try {
            boolean res = sendCardOrderService.finishOrderById(sendOrderId);
            if(!res) {
                result.setIsSucceed(false);
                result.setFailMsg("请勿重复处理");
                return result;
            }

            // 归还卡牌.
            userCardBookService.putIntoCard(orderModel.getSenderUid(), orderModel.getCardId());
        }
        catch (Exception e) {
            LOGGER.error("act=rejectReceiveCard error receiverUid={} orderModel={}", uid, JsonMapper.toJson(orderModel));
            result.setIsSucceed(false);
            result.setFailMsg("服务异常");
            return result;
        }
        finally {
            // unlcok
        }

        chatMsgFacade.sendChatMsg(uid, orderModel.getSenderUid(), "谢谢你啦，不过这张卡牌我暂时用不到呢～");

        LOGGER.info("act=rejectReceiveCard uid={} orderModel={}", uid, JsonMapper.toJson(orderModel));
        result.setIsSucceed(true);
        return result;
    }

    /**
     * 激活卡牌 添加事务@Transactional 并发有问题，锁范围一定要和事务的范围相同.
     */
    public ActivateCardResultBO activateCard(String uid, Long cardId) {
        ActivateCardResultBO result = new ActivateCardResultBO();

        CardModel cardModel = cardDomainService.getCard(cardId);
        if(cardModel == null) {
            result.setIsSucceed(false);
            result.setFailMsg("卡牌已下线");
            return result;
        }

        CardGroupModel cardGroupModel = cardDomainService.getCardGroup(cardModel.getCardGroupId());
        if(cardGroupModel == null) {
            result.setIsSucceed(false);
            result.setFailMsg("卡牌已下线");
            return result;
        }

        Card bookCard = userCardBookService.queryUserCard(uid, cardId);
        if(bookCard == null) {
            result.setIsSucceed(false);
            result.setFailMsg("卡牌不存在");
            return result;
        }

        boolean isCardGroupCompleted = true;
        // lock
        try {
            CardGroup userCardGroup = userCardBookService.getUserCardGroup(uid, cardModel.getCardGroupId());
            if(userCardGroup == null || CollectionUtils.isEmpty(userCardGroup.getCardList())) {
                result.setIsSucceed(false);
                result.setFailMsg("卡牌不存在");
                return result;
            }

            CardGroup.ExtraInfo extInfo = userCardGroup.getExtraInfo();
            // 如果已经全部集齐且卡组的卡牌全部激活了.
            if(extInfo != null && extInfo.isCollectCompleted()) {
                result.setIsSucceed(false);
                result.setFailMsg("卡牌已激活");
                return result;
            }

            // 锁定卡牌.
            boolean res = userCardBookService.lockCard(uid, cardId);
            if(!res) {
                result.setIsSucceed(false);
                result.setFailMsg("卡牌已激活");
                return result;
            }

            Map<Long, Card> userCardMap = userCardGroup.getCardList().stream().collect(Collectors.toMap(Card::getId, card -> card));
            List<CardModel> cardModelList = cardGroupModel.getCardList();
            for(CardModel card : cardModelList) {
                if(card.getId().equals(cardId)) {
                    continue;
                }

                Card userCard = userCardMap.get(card.getId());
                if(userCard == null || userCard.getExtraInfo() == null || !userCard.getExtraInfo().isLock()) {
                    isCardGroupCompleted = false;
                }
            }

            if(isCardGroupCompleted) {
                res = userCardBookService.updateUserCardGroupExtraInfo(uid, cardModel.getCardGroupId(), cardId, true);
                if(!res) {
                    LOGGER.error("act=activateCard updateUserCardGroupExtraInfo fail uid={} cardId={}", uid, cardId);
                    throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
                }
            }

            // 卡组集齐.
            if(isCardGroupCompleted) {
                // todo 广播卡组集齐事件.
            }

            result.setIsSucceed(true);

            LOGGER.info("act=activateCard end uid={} cardId={}", uid, cardId);
            return result;
        }
        finally {
            // unlock
        }
    }

    /**
     * 从用户卡册取出指定卡，只取出一张.
     */
    public boolean takeOutUserCard(String uid, Long cardId, String sceneKey) {
        if(StringUtils.isEmpty(uid) || cardId == null) {
            throw new CollectCardException(CollectCardErrorCode.PARAM_ERROR);
        }
        SceneModel sceneModel = sceneService.getSceneModelByKey(sceneKey);
        if(sceneModel == null){
            throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
        }
        List<Long> cardGroup = sceneModel.getCardGroup();
        if(CollectionUtils.isEmpty(cardGroup)){
            throw new CollectCardException(CollectCardErrorCode.DATA_ERROR);
        }
        return userCardBookService.takeOutCard(uid, cardId, cardGroup);
    }
}
