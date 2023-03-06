package com.github.interaction.comment.app.audit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核策略.
 *
 * @author yanghuan
 */
@AllArgsConstructor
@Getter
public enum AuditPolicyEnum {

    /**
     * 不审核
     */
    NO_AUDIT(0,"不审核"),

    /**
     * 先审后发
     */
    AUDIT_FIRST(1,"先审后发"),

    /**
     * 人审核通过
     */
    AFTER_AUDIT(2,"先发后审"),

    ;

    int type;
    String desc;
}
