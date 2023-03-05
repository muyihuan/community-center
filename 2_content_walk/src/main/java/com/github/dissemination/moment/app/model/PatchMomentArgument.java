package com.github.dissemination.moment.app.model;

import lombok.Data;

/**
 * 刷新朋友圈参数.
 *
 * @author yanghuan
 */
@Data
public class PatchMomentArgument {

    /**
     * 要刷新哪个用户的朋友圈.
     */
    private String user;

    /**
     * 只刷新这个时间点到现在发生的变化，保证该用户这个时间段的动态完整.
     * 不填写会刷新全部时间的变化,建议设置用户上次登陆时间或者不填.
     */
    private Long refreshAfterThisTime;
}
