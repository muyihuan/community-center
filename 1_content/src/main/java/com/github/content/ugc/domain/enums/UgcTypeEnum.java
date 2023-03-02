package com.github.content.ugc.domain.enums;

import lombok.Getter;

/**
 * ugc内容的类型.
 *
 * @author yanghuan
 */
@Getter
public enum UgcTypeEnum {

    TEXT(0, "文本内容"),
    IMAGE(1, "图片内容"),
    AUDIO(2, "音频内容"),
    VIDEO(3, "视频内容"),

    ;

    private final int code;
    private final String desc;

    UgcTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UgcTypeEnum getByCode(int code) {
        for (UgcTypeEnum value : UgcTypeEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }

        throw new RuntimeException("UgcTypeEnum can't find : " + code);
    }
}
