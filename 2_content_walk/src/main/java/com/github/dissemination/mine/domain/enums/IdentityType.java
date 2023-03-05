package com.github.dissemination.mine.domain.enums;

import lombok.Getter;

/**
 * 身份类型.
 *
 * @author yanghuan
 */
@Getter
public enum IdentityType {

    MYSELF(0, "本人"),
    FRIEND(1, "好友"),
    STRANGER(2, "陌生人"),
    SYSTEM(3, "系统"),
    ;

    private final int code;
    private final String desc;

    IdentityType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
