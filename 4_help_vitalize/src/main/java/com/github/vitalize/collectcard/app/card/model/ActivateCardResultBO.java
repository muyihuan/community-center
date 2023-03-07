package com.github.vitalize.collectcard.app.card.model;

import lombok.Data;

/**
 * 激活卡结果.
 *
 * @author yanghuan
 */
@Data
public class ActivateCardResultBO {

    /**
     * 是否成功.
     */
    private Boolean isSucceed;

    /**
     * 失败原因.
     */
    private String failMsg;
}
