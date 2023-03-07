package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

/**
 * 拒绝收卡结果.
 *
 * @author yanghuan
 */
@Data
public class RejectReceiveCardResultBO {

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
        return "RejectReceiveCardResultBO{" +
                "isSucceed=" + isSucceed +
                ", failMsg='" + failMsg + '\'' +
                '}';
    }
}
