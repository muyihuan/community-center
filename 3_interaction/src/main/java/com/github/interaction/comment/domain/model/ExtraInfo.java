package com.github.interaction.comment.domain.model;

import lombok.Data;

/**
 * 扩展信息.
 *
 * @author yanghuan
 */
@Data
public class ExtraInfo {

    /**
     * 作者用户信息，减少用户信息的查询，提高评论查询效率
     */
    private UserInfo authorInfo;

    /**
     * 被回复用户信息，减少用户信息的查询，提高评论查询效率
     */
    private UserInfo repliedInfo;

    /**
     * 评论发布所在的ip归属地
     */
    private String ipHome;
}
