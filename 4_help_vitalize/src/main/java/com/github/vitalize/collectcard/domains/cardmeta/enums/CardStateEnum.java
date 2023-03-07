package com.github.vitalize.collectcard.domains.cardmeta.enums;

import lombok.Getter;

/**
 * 卡牌的状态.
 *
 * @author yanghuan
 */
@Getter
public enum CardStateEnum {

    NORMAL(0, "正常状态"),
    DEL(1, "删除状态"),

    ;

    private final int code;
    private final String desc;

    CardStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardStateEnum getByCode(int status) {
        for (CardStateEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }

        throw new RuntimeException("CardStateEnum can't find : " + status);
    }
}
