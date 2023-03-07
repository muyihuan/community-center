package com.github.vitalize.collectcard.app.card;

import org.springframework.stereotype.Service;

/**
 * 限制相关处理.
 *
 * @author yanghuan
 */
@Service
public class LimitService {

    private static final String KEY_USER_ISSUED_SUCCESS = "c:c:issued:success:";
    private static final String KEY_USER_SEND = "c:c:send:user:";
    private static final String KEY_USER_RECEIVE = "c:c:receive:user:";

    private static final int ONE_DAY = 86400;

    /**
     * 获取用户今日成功获取卡的次数.
     */
    public int getUserTodayIssuedSuccCount(String uid) {
        return 0;
    }

    /**
     * 增加用户今日成功获取卡的次数.
     */
    public void incrUserTodayIssuedSuccCount(String uid) {

    }

    /**
     * 获取用户今日赠卡的次数.
     */
    public int getUserTodaySendCardCount(String uid) {
        return 0;
    }

    /**
     * 增加用户今日赠卡的次数.
     */
    public void incrUserTodaySendCardCount(String uid) {

    }

    /**
     * 获取用户今日收卡的次数.
     */
    public int getUserTodayReceiveCardCount(String uid) {
        return 0;
    }

    /**
     * 增加用户今日收卡的次数.
     */
    public void incrUserTodayReceiveCardCount(String uid) {

    }
}
