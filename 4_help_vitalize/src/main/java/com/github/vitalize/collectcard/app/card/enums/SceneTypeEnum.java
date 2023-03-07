package com.github.vitalize.collectcard.app.card.enums;

import lombok.Getter;

/**
 * 场景类型.
 *
 * @author yanghuan
 */
@Getter
public enum SceneTypeEnum {

    NONE(0, "无"),

    ;

    private final int code;
    private final String desc;

    SceneTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SceneTypeEnum getByCode(int code) {
        for (SceneTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("SceneTypeEnum can't find : " + code);
    }
}
