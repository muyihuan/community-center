package com.github.vitalize.collectcard.infra.rpc;

import org.springframework.stereotype.Service;

/**
 * 消息三方.
 *
 * @author yanghuan
 */
@Service
public class ChatMsgFacade {

    /**
     * 发送弱文案.
     */
    public void sendWeakHint(String fromUid, String toUid, String text){

    }

    /**
     * 发送送卡消息卡片.
     */
    public void sendSendCardCommonMsgCard(String fromUid, String toUid, String showUid, Long orderId, Long remainMillis, String iconUrl,String sceneKey) {

    }

    /**
     * 发送送卡消息卡片 无按钮.
     */
    public void sendSendCardCommonMsgCardNoButton(String fromUid, String toUid, String showUid, Long orderId, Long remainMillis, String iconUrl) {

    }

    /**
     * 私聊消息.
     */
    public void sendChatMsg(String fromUid, String toUid, String msg) {

    }
}
