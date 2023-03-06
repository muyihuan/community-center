package com.github.interaction.comment.app.commet.enums;

import lombok.Getter;

/**
 * 评论的可见性设置.
 *
 * @author yanghuan
 */
@Getter
public enum CommentVisibilityEnum {

    DEF(0, "默认-系统未设置可见范围"),
    COMMENT_SELF_VIEW(1, "只允许自己可见"),

    ;

    private final int code;
    private final String desc;

    CommentVisibilityEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommentVisibilityEnum getByCode(int code) {
        for (CommentVisibilityEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("CommentStateEnum can't find : " + code);
    }
}
