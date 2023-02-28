package com.github.content.feed.domain.model.ugc;

import lombok.Data;

/**
 * 猜猜信息
 * @author wenju
 */
@Data
public class LotteryContentModel {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userIconImg;

    /**
     * 猜猜内容
     */
    private String content;

    /**
     * 猜猜图片
     */
    private String url;

    /**
     * 猜猜小图片
     */
    private String urlSmall;

    /**
     * 猜猜状态，是否结束
     */
    private int result;

    /**
     * 猜猜结束时间
     */
    private long stopBuyTime;
}
