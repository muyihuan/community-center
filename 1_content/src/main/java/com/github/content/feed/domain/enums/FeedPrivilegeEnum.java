package com.github.content.feed.domain.enums;

import lombok.Getter;

/**
 * feed 可见范围.
 *
 * @author yanghuan
 */
@Getter
public enum FeedPrivilegeEnum {

    SELF_VISIBLE(0, "仅自己可见"),
    FRIEND_VISIBLE(1, "仅好友可见"),
    STRANGER_VISIBLE(2, "仅陌生人可见"),
    ALL_VISIBLE(3, "全部人可见"),
    ;

    private final int code;
    private final String desc;

    FeedPrivilegeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FeedPrivilegeEnum getByCode(int code) {
        for (FeedPrivilegeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("FeedPrivilegeEnum can't find : " + code);
    }
}
