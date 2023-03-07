package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

/**
 * 赠卡结果.
 *
 * @author yanghuan
 */
@Data
public class SendCardResultBO {

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
        return "SendCardResultBO{" +
                "isSucceed=" + isSucceed +
                ", failMsg='" + failMsg + '\'' +
                '}';
    }
}
