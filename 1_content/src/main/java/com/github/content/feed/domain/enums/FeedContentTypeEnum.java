package com.github.content.feed.domain.enums;

import lombok.Getter;

/**
 * feed的类型.
 *
 * @author yanghuan
 */
@Getter
public enum FeedContentTypeEnum {

    TEXT(0, "文本"),
    IMAGE(1, "图片"),
    AUDIO(2, "语音"),
    VIDEO(3, "视频"),
    LINK(4, "链接"),
    FORWARD(5, "转发"),

    ;

    private final int type;
    private final String desc;

    FeedContentTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FeedContentTypeEnum getByCode(int type) {
        for (FeedContentTypeEnum value : FeedContentTypeEnum.values()) {
            if (value.getType() == type) {
                return value;
            }
        }

        throw new RuntimeException("FeedContentType can't find : " + type);
    }
}
