package com.github.vitalize.collectcard.app.card.enums;

import lombok.Getter;

/**
 * 卡组的状态.
 *
 * @author yanghuan
 */
@Getter
public enum CardGroupCollectStateEnum {

    LOCK(0, "未解锁"),
    TO_ACTIVATED(1, "待激活"),
    ACTIVATED(2, "已激活"),
    OK(3, "已集齐"),

    ;

    private final int code;
    private final String desc;

    CardGroupCollectStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardGroupCollectStateEnum getByCode(int status) {
        for (CardGroupCollectStateEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }

        throw new RuntimeException("CardStateEnum can't find : " + status);
    }
}
