package com.github.content.feed.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核结果.
 *
 * @author yanghuan
 */
@AllArgsConstructor
@Getter
public enum AuditResultEnum {

    /**
     *
     */
    PASS(1,"通过"),
    REJECTED(2,"拒绝"),

    ;

    int code;
    String desc;

    public static AuditResultEnum getByCode(int code) {
        for (AuditResultEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new RuntimeException("AuditResultEnum can't code : " + code);
    }
}
