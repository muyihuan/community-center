package com.github.interaction.comment.domain.model;

import lombok.Data;

/**
 * 用户信息.
 *
 * @author yanghuan
 */
@Data
public class UserInfo {

    /**
     * id.
     */
    private String uid;

    /**
     * 用户名.
     */
    private String userName;

    /**
     * 用户头像.
     */
    private String userIcon;

    /**
     * 性别.
     */
    private String gender;
}
