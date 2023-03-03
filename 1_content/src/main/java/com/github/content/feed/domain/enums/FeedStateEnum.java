package com.github.content.feed.domain.enums;

import lombok.Getter;

/**
 * feed 状态.
 *
 * @author yanghuan
 */
@Getter
public enum FeedStateEnum {

    NORMAL(0, "正常状态"),
    DEL(1, "删除状态"),

    ;

    private final int status;
    private final String desc;

    FeedStateEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static FeedStateEnum getByCode(int status) {
        for (FeedStateEnum value : values()) {
            if (value.getStatus() == status) {
                return value;
            }
        }

        throw new RuntimeException("FeedStateEnum can't find : " + status);
    }
}
