package com.github.vitalize.collectcard.domains.cardmeta.enums;

import lombok.Getter;

/**
 * 卡包的等级.
 *
 * @author yanghuan
 */
@Getter
public enum CardBagLevelEnum {

    ORDINARY(0, "普通卡包"),
    SILVER(1, "银卡包"),
    GOLD(2, "金卡包"),

    ;

    private final int code;
    private final String desc;

    CardBagLevelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CardBagLevelEnum getByCode(int code) {
        for (CardBagLevelEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("CardLevelEnum can't find : " + code);
    }
}
