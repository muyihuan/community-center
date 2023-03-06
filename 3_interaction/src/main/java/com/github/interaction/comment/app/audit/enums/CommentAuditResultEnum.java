package com.github.interaction.comment.app.audit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核结果.
 *
 * @author yanghuan
 */
@AllArgsConstructor
@Getter
public enum CommentAuditResultEnum {

    /**
     * 机器审核通过
     */
    MACHINE_PASS(0,"机器审核通过"),

    /**
     * 机器审核拒绝
     */
    MACHINE_REJECT(1,"机器审核拒绝"),

    /**
     * 人审核通过
     */
    PEOPLE_PASS(2,"人审核通过"),

    /**
     * 审核拒绝
     */
    PEOPLE_REJECT(3,"审核拒绝"),

    ;

    int type;
    String desc;
}
