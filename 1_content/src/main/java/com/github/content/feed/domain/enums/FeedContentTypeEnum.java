package com.github.content.feed.domain.enums;

import lombok.Getter;

/**
 * feed的内容类型.
 *
 * @author yanghuan
 */
@Getter
public enum FeedContentTypeEnum {

    //******************************************************************************//
    //*****这里的类型不会承载内容的丰富性，后续需要拓展一个 UGC_TYPE(用户产出内容)类型，因为同样为文**//
    //*****字类型的feed，其文字内容向也有很多种，比如 作文、诗词、日常等等***********************//
    //******************************************************************************//

    TEXT(0, "短文本"),
    IMAGE(1, "图片"),
    AUDIO(2, "语音"),
    VIDEO(3, "新视频"),
    LINK(4, "链接"),
    FORWARD(5, "转发"),

    ;

    /**
     * type
     */
    private final int type;

    /**
     * 描述
     */
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
