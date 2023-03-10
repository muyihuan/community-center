package com.github.content.feed.domain.enums;

import lombok.Getter;

/**
 * feed的系统可见范围，是系统侧设置的可见范围，优先级大于用户设置的可见范围.
 *
 * @author yanghuan
 */
@Getter
public enum FeedSystemPrivilegeEnum {

    DEF(0, "默认-系统未设置可见范围"),
    FEED_SELF_VISIBLE(1, "feed只允许自己可见"),
    UGC_SELF_VISIBLE(2, "ugc内容只允许自己可见"),

    ;

    private final int code;
    private final String desc;

    FeedSystemPrivilegeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
