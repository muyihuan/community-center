package com.github.vitalize.collectcard.domains.cardmeta.enums;

import lombok.Getter;

/**
 * 卡组的状态.
 *
 * @author yanghuan
 */
@Getter
public enum CardGroupStateEnum {

    NORMAL(0, "正常状态"),
    DEL(1, "删除状态"),

    ;

    private final int code;
    private final String desc;

    CardGroupStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardGroupStateEnum getByCode(int status) {
        for (CardGroupStateEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }

        throw new RuntimeException("CardStateEnum can't find : " + status);
    }
}
