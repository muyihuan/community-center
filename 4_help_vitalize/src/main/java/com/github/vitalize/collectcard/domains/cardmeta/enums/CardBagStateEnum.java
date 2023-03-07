package com.github.vitalize.collectcard.domains.cardmeta.enums;

import lombok.Getter;

/**
 * 卡包的状态.
 *
 * @author yanghuan
 */
@Getter
public enum CardBagStateEnum {

    NORMAL(0, "正常状态"),
    DEL(1, "删除状态"),

    ;

    private final int code;
    private final String desc;

    CardBagStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardBagStateEnum getByCode(int status) {
        for (CardBagStateEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }

        throw new RuntimeException("CardStateEnum can't find : " + status);
    }
}
