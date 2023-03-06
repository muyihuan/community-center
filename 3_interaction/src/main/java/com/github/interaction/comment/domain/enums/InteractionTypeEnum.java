package com.github.interaction.comment.domain.enums;

import lombok.Getter;

/**
 * 评论互动类型.
 *
 * @author yanghuan
 */
@Getter
public enum InteractionTypeEnum {

    LIKE(0, "点赞"),

    // ...

    ;

    private final int status;
    private final String desc;

    InteractionTypeEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
