package com.github.content.feed.domains.enums;

import lombok.Getter;

/**
 * feed 来源的类型
 * @author yanghuan
 */
@Getter
public enum FeedSourceTypeEnum {

    NONE(0, "无"),
    ;

    private final int code;
    private final String desc;

    FeedSourceTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FeedSourceTypeEnum getByCode(int code) {
        for (FeedSourceTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("FeedSourceType can't find : " + code);
    }
}
