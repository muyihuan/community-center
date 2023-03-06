package com.github.interaction.comment.app.commet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 锚点类型.
 *
 * @author yanghuan
 */
@AllArgsConstructor
@Getter
public enum AnchorTypeEnum {

    SELF(1,"定点到本条评论"),
    COMMENT_TOPIC(2,"定点到宿主用户和该评论的作者的所有评论"),
    REPLIED_COMMENT(3,"定点到回复评论的用户和发布作者的所有评论"),

    ;

    int code;
    String desc;

    public static AnchorTypeEnum getByCode(int code) {
        for (AnchorTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("AnchorTypeEnum can't find : " + code);
    }
}
