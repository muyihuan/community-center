package com.github.dissemination.moment.app.model;

import lombok.Data;

/**
 * 用户进入app信息.
 *
 * @author yanghuan
 */
@Data
public class EnterAppInfo {

    /**
     * 用户ID.
     */
    private String uid;

    /**
     * 上次登陆时间.
     */
    private Long lastLoginTime;
}
