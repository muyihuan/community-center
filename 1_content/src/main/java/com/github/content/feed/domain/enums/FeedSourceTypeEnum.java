package com.github.content.feed.domain.enums;

import lombok.Getter;

/**
 * 来源的类型.
 *
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
}
