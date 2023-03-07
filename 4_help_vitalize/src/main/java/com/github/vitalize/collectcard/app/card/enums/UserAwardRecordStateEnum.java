package com.github.vitalize.collectcard.app.card.enums;

import lombok.Getter;

/**
 * 奖励记录状态.
 *
 * @author yanghuan
 */
@Getter
public enum UserAwardRecordStateEnum {

    INIT(0, "初始状态"),
    FINISH(1, "完成状态"),

    ;

    private final int code;
    private final String desc;

    UserAwardRecordStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserAwardRecordStateEnum getByCode(int status) {
        for (UserAwardRecordStateEnum value : values()) {
            if (value.getCode() == status) {
                return value;
            }
        }

        throw new RuntimeException("UserAwardRecordStateEnum can't find : " + status);
    }
}
