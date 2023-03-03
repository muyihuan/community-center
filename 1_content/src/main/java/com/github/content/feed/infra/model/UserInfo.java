package com.github.content.feed.infra.model;

import lombok.Data;

/**
 * 用户基本信息.
 *
 * @author yanghuan
 */
@Data
public class UserInfo {

    /**
     * 用户id.
     */
    private String id;

    /**
     * 用户昵称.
     */
    private String username;

    /**
     * 用户头像.
     */
    private String icon;

    /**
     * 用户性别.
     */
    private String gender;
}
