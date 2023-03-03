package com.github.content.feed.app.checker;

import lombok.Data;

/**
 * 校验结果及原因.
 *
 * @author yanghuan
 */
@Data
public class CheckResult {

    private static final CheckResult SUCCESS;
    static {
        SUCCESS = new CheckResult();
        SUCCESS.setSuccess(true);
    }

    private String resultMsg;
    private Boolean success;

    /**
     * 成功.
     *
     * @return 成功.
     */
    public static CheckResult success() {
        return SUCCESS;
    }

    /**
     * 失败.
     *
     * @param msg 失败原因.
     * @return 失败.
     */
    public static CheckResult fail(String msg) {
        CheckResult checkResult = new CheckResult();
        checkResult.setSuccess(false);
        checkResult.setResultMsg(msg);
        return checkResult;
    }
}
