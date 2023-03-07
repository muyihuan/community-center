package com.github.vitalize.collectcard.exception;

import lombok.Getter;

/**
 * 起始code 90001.
 *
 * @author yanghuan
 */
@Getter
public enum CollectCardErrorCode {

    PARAM_ERROR(90001, "参数 有误"),
    CONTENT_TYPE_NOT_SUPPORT_ERROR(90002, "不支持该内容类型"),
    OPERATE_FREQUENTLY_ERROR(90003, "操作频繁 执行失败"),
    OPT_ILLEGAL_ERROR(90005, "操作不合法"),
    DATA_ERROR(90006, "数据异常"),
    FINISH(90007, "活动已结束"),

    ;

    private final int code;
    private final String msg;

    CollectCardErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
