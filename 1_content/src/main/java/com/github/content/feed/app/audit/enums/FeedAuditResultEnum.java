package com.github.content.feed.app.audit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核结果.
 *
 * @author yanghuan
 */
@AllArgsConstructor
@Getter
public enum FeedAuditResultEnum {

    MACHINE_PASS(0,"机器审核通过"),
    PEOPLE_PASS(1,"人审核通过"),
    REJECT(2,"审核拒绝"),
    REJECT_SELF_VIEW(3, "审核未通过 执行feed自见"),
    MACHINE_REJECT(4,"机器审核拒绝"),

    ;

    int type;
    String desc;
}
