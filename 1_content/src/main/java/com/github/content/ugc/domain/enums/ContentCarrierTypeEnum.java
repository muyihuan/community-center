package com.github.content.ugc.domain.enums;

import lombok.Getter;

/**
 * 内容载体类型.
 *
 * @author yanghuan
 */
@Getter
public enum ContentCarrierTypeEnum {

    NONE(0, "无"),
    FEED(1, "feed"),

    ;

    private final int code;
    private final String desc;

    ContentCarrierTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
