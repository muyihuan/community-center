package com.github.interaction.comment.exception;

import lombok.Getter;

/**
 * 起始code 50001.
 *
 * @author yanghuan
 */
@Getter
public enum CommentErrorCode {

    COMMENT_PARAM_ERROR(50001, "参数 有误"),
    CONTENT_TYPE_NOT_SUPPORT_ERROR(50002, "不支持该内容类型"),
    OPERATE_FREQUENTLY_ERROR(50003, "操作频繁 执行失败"),
    COMMENT_NOT_EXIST(50004, "评论不存在"),
    OPT_ILLEGAL_ERROR(50005, "操作不合法"),
    DATA_ERROR(50006, "数据异常"),

    ;

    private final int code;
    private final String msg;

    CommentErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
