package com.github.content.feed.app.model;

import lombok.Setter;

/**
 * 创建feed的结果
 * @author yanghuan
 */
@Setter
public class CreateFeedResult {

    /**
     * feed的id
     */
    private Long feedId;

    /**
     * 是否成功
     */
    private boolean isSucceed;

    /**
     * 失败原因
     */
    private String failMsg;

    public boolean isSucceed(){
        return isSucceed;
    }

    public Long getFeedId() {
        return isSucceed ? feedId : null;
    }

    public String getFailMsg() {
        return failMsg;
    }
}
