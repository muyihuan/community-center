package com.github.dissemination.mine.exception;


/**
 * 内容传播异常码 起始code 30001.
 *
 * @author yanghuan
 */
public enum ContentWalkErrorCode {

    PARAM_ERROR(30001, "参数异常"),
    DATA_ERROR(30002, "数据异常"),
    ILLEGAL_OPT_ERROR(30003, "不合法操作"),

    ;

    private final int code;
    private final String msg;

    ContentWalkErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
