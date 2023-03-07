package com.github.vitalize.collectcard.app.card;

import com.github.infrastructure.json.JsonMapper;
import com.github.vitalize.collectcard.app.card.enums.SendCardOrderStateEnum;
import com.github.vitalize.collectcard.app.card.model.SendCardOrderModel;
import com.github.vitalize.collectcard.app.card.repo.SendCardOrderRepository;
import com.github.vitalize.collectcard.app.card.repo.model.SendCardOrderDO;
import com.github.vitalize.collectcard.domains.cardmeta.CardDomainService;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardModel;
import com.github.vitalize.collectcard.domains.usercard.UserCardBookService;
import com.github.vitalize.collectcard.infra.rpc.ChatMsgFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 赠卡运送订单处理.
 *
 * @author yanghuan
 */
@Service
public class SendCardOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendCardOrderService.class);

    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, runnable -> new Thread(runnable, "dealExpireOrder"));

    @Autowired
    private SendCardOrderRepository sendCardOrderRepository;

    @Autowired
    private UserCardBookService userCardBookService;

    @Autowired
    private CardDomainService cardDomainService;

    @Autowired
    private ChatMsgFacade chatMsgFacade;

    private static final String KEY_COLLECT_CARD_DEAL_EXPIRE_ORDER_LOCK = "c:c:expire:order:lock:";

    private static final int ONE_DAY = 86400;

    @PostConstruct
    public void init() {
        // 十分钟执行一次.
        executorService.scheduleAtFixedRate(this::dealExpireOrder, 0, 10, TimeUnit.MINUTES);
    }

    /**
     * 创建订单.
     */
    public Long createOrder(String senderUid, String receiverUid, Long cardId, Integer expireHours) {
        SendCardOrderDO sendCardOrderDO = new SendCardOrderDO();
        sendCardOrderDO.setSenderUid(senderUid);
        sendCardOrderDO.setReceiverUid(receiverUid);
        sendCardOrderDO.setCardId(cardId);
        sendCardOrderDO.setExpireTime(DateUtils.addHours(new Date(), expireHours));
        return sendCardOrderRepository.createOrder(sendCardOrderDO);
    }

    /**
     * 通过ID查询订单.
     */
    public SendCardOrderModel getOrderById(Long id) {
        if(id == null) {
            return null;
        }

        SendCardOrderDO sendCardOrderDO = sendCardOrderRepository.getOrderById(id);
        if(sendCardOrderDO == null || sendCardOrderDO.getStatus() == SendCardOrderStateEnum.DEL.getCode()) {
            return null;
        }

        SendCardOrderModel sendCardOrderModel = new SendCardOrderModel();
        sendCardOrderModel.setId(sendCardOrderDO.getId());
        sendCardOrderModel.setSenderUid(sendCardOrderDO.getSenderUid());
        sendCardOrderModel.setReceiverUid(sendCardOrderDO.getReceiverUid());
        sendCardOrderModel.setCardId(sendCardOrderDO.getCardId());
        sendCardOrderModel.setExpireTime(sendCardOrderDO.getExpireTime());
        sendCardOrderModel.setCreateTime(sendCardOrderDO.getCreateTime());
        return sendCardOrderModel;
    }

    /**
     * 结束订单.
     */
    public boolean finishOrderById(Long id) {
        if(id == null) {
            return true;
        }

        return sendCardOrderRepository.delOrder(id);
    }

    /**
     * 处理过期订单.
     * 一期数据量少使用定时任务处理，后面数据多之后，使用延时消息实现.
     */
    private void dealExpireOrder() {
        LOGGER.info("act=dealExpireOrder start");

        // lock
        try {
            while(true) {
                List<SendCardOrderDO> sendCardOrderDOList = sendCardOrderRepository.queryExpireOrderList(100);
                if(CollectionUtils.isEmpty(sendCardOrderDOList)) {
                    return;
                }

                for(SendCardOrderDO order : sendCardOrderDOList) {
                    boolean res = finishOrderById(order.getId());
                    if(!res) {
                        // 订单已经被处理
                        continue;
                    }

                    // 归还用户卡牌
                    userCardBookService.putIntoCard(order.getSenderUid(), order.getCardId());

                    // 发送卡牌退回弱文案
                    CardModel card = cardDomainService.getCard(order.getCardId());
                    if(card != null) {
                        chatMsgFacade.sendWeakHint(order.getReceiverUid(), order.getSenderUid(), "对方未领取卡牌，已将【"+ card.getName() +"】退还至您的卡册");
                    }

                    LOGGER.info("act=dealExpireOrder order={}", JsonMapper.toJson(order));
                }

                Thread.sleep(1000);
            }
        }
        catch (Exception e) {
            LOGGER.error("act=dealExpireOrder error", e);
        }
        finally {
            // unlock
        }
    }
}
