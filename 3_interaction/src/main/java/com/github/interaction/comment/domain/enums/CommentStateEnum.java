package com.github.interaction.comment.domain.enums;

import lombok.Getter;

/**
 * 评论的状态.
 *
 * @author yanghuan
 */
@Getter
public enum CommentStateEnum {

    NORMAL(0, "正常状态"),
    DEL(1, "删除状态"),

    ;

    private final int code;
    private final String desc;

    CommentStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommentStateEnum getByCode(int status) {
        for (CommentStateEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }

        throw new RuntimeException("CommentStateEnum can't find : " + status);
    }
}
