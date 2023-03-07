package com.github.vitalize.collectcard.domains.cardmeta.enums;

import lombok.Getter;

/**
 * 卡牌的等级.
 *
 * @author yanghuan
 */
@Getter
public enum CardLevelEnum {

    ONE_STAR(0, "一星"),
    TWO_STAR(1, "二星"),
    THREE_STAR(2, "三星"),
    FOUR_STAR(3, "四星"),

    ;

    private final int code;
    private final String desc;

    CardLevelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardLevelEnum getByCode(int code) {
        for (CardLevelEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("CardLevelEnum can't find : " + code);
    }
}
