package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

/**
 * 收卡结果.
 *
 * @author yanghuan
 */
@Data
public class ReceiveCardResultBO {

    /**
     * 是否成功.
     */
    private Boolean isSucceed;

    /**
     * 失败原因.
     */
    private String  failMsg;

    @Override
    public String toString() {
        return "ReceiveCardResultBO{" +
                "isSucceed=" + isSucceed +
                ", failMsg='" + failMsg + '\'' +
                '}';
    }
}
