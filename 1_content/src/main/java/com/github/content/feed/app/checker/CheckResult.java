package com.github.content.feed.app.checker;

import lombok.Data;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 创建处理器，可以对创建UGC前、创建FEED前、创建 FEED 后进行扩展
 * @createTime 2021年05月10日 17:35:00
 */
@Data
public class CheckResult {
    private static final CheckResult SUCCESS_INSTANCE;

    static {
        SUCCESS_INSTANCE = new CheckResult();
        SUCCESS_INSTANCE.setSuccess(true);
    }

    private String resultMsg;
    private Boolean success;

    public static CheckResult fail(String msg) {
        CheckResult checkResult = new CheckResult();
        checkResult.setSuccess(Boolean.FALSE);
        checkResult.setResultMsg(msg);
        return checkResult;
    }
    public static CheckResult success() {
        return SUCCESS_INSTANCE;
    }
}
