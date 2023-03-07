package com.github.vitalize.collectcard.app.card.repo.model;

import lombok.Data;

/**
 * 扩展信息.
 *
 * @author yanghuan
 */
@Data
public class ExtraInfo {

    /**
     * 原因.
     */
    private String reason;

    /**
     * 卡牌ID.
     */
    private Long cardId;

    /**
     * 卡组ID.
     */
    private Long cardGroupId;
}
