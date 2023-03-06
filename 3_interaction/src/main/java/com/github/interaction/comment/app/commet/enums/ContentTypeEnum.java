package com.github.interaction.comment.app.commet.enums;

import lombok.Getter;

/**
 * 评论内容类型.
 *
 * @author yanghuan
 */
@Getter
public enum ContentTypeEnum {

    TEXT(0, "文本类型"),
    IMAGE(1, "单图类型"),
    AUDIO(2, "语音类型"),
    ;

    private final int type;
    private final String desc;

    ContentTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ContentTypeEnum getByCode(int type) {
        for (ContentTypeEnum value : ContentTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }

        throw new RuntimeException("ContentType can't find : " + type);
    }
}
