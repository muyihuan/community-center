package com.github.content.feed.app.model.create;

import lombok.Setter;

/**
 * feed创建的结果.
 *
 * @author yanghuan
 */
@Setter
public class CreateFeedResult {

    /**
     * feed的id.
     */
    private Long feedId;

    /**
     * 是否成功.
     */
    private boolean isSucceed;

    /**
     * 如果失败，对应失败原因.
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
