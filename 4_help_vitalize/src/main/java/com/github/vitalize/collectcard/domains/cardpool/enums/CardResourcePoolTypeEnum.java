package com.github.vitalize.collectcard.domains.cardpool.enums;

import lombok.Getter;

/**
 * 池子资源.
 *
 * @author yanghuan
 */
@Getter
public enum CardResourcePoolTypeEnum {

    ALL(0, "全量池"),

    ;

    private final int code;
    private final String desc;

    CardResourcePoolTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
