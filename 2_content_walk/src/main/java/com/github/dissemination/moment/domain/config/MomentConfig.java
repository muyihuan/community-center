package com.github.dissemination.moment.domain.config;

import lombok.Data;

import java.util.Set;

/**
 * 朋友圈相关配置.
 *
 * @author yanghuan
 */
@Data
public class MomentConfig {

    /**
     * 官方账号uid列表，官方账号的朋友圈不同步好友动态。但官方账号发布的动态会同步给所有人.
     * 注：官方账号确认好该用户的动态是否要同步给所有人，思考好再配置.
     */
    private Set<String> officialAccounts;

    /**
     * 活跃用户登录时间值判断：90天，距离上次登录大于90天为非活跃用户.
     *
     * 假设修改前值为: a, 修改后值为: b.
     * b > a: a到b之间的用户还要保持拉好友动态，等过了b - a天数之后就不用再处理第一步了.
     * b < a：没有影响.
     */
    private long activeUserLoginDurationDay = 90;

    /**
     * 用户朋友圈动态数量阈值.
     */
    private int momentThreshold = 200;

    /**
     * 每天最多清理多少人的朋友圈.
     */
    private int cleanMomentMaxCount = 1000000;

    /**
     * 朋友圈第一页展示的数量.
     */
    private int firstPageCount = 20;

    /**
     * 一次最多复制一个好友的动态到朋友圈的个数.
     */
    private int maxCopyOneFriendContentSize = 30;

    public static MomentConfig get() {
        return new MomentConfig();
    }
}
