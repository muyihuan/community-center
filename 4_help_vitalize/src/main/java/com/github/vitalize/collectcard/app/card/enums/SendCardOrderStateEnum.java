package com.github.vitalize.collectcard.app.card.enums;

import lombok.Getter;

/**
 * 送卡订单状态.
 *
 * @author yanghuan
 */
@Getter
public enum SendCardOrderStateEnum {

    NORMAL(0, "正常状态"),
    DEL(1, "删除状态"),

    ;

    private final int code;
    private final String desc;

    SendCardOrderStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SendCardOrderStateEnum getByCode(int status) {
        for (SendCardOrderStateEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }

        throw new RuntimeException("SendCardOrderStateEnum can't find : " + status);
    }
}
