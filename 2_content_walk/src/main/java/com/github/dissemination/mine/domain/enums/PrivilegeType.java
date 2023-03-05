package com.github.dissemination.mine.domain.enums;

import lombok.Getter;

/**
 * 个人动态可见范围类型.
 *
 * @author yanghuan
 */
@Getter
public enum PrivilegeType {

    SELF_VISIBLE(0, "仅自己可见"),
    FRIEND_VISIBLE(1, "仅好友可见"),
    STRANGER_VISIBLE(2, "仅陌生人可见"),
    ALL_VISIBLE(3, "全部人可见"),
    ;

    private final int code;
    private final String desc;

    PrivilegeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
