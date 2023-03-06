package com.github.interaction.comment.app.commet.model;

import lombok.Setter;

/**
 * 创建feed的结果.
 *
 * @author yanghuan
 */
@Setter
public class CreateCommentResult {

    /**
     * 评论的唯一ID
     */
    private Long commentId;

    /**
     * 是否成功
     */
    private boolean isSucceed;

    /**
     * 失败原因码
     */
    private Integer failCode;

    /**
     * 失败原因
     */
    private String failMsg;

    public boolean isSucceed(){
        return isSucceed;
    }

    public Long getCommentId() {
        return isSucceed ? commentId : null;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public Integer getFailCode() {
        return failCode;
    }
}
