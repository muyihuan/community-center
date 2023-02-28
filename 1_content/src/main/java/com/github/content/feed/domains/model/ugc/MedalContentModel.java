package com.github.content.feed.domains.model.ugc;

import lombok.Data;

/**
 * 奖牌信息
 * @author lishipeng
 */
@Data
public class MedalContentModel {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 徽章的id
     */
    private int medalId;

    /**
     * 徽章的名称
     */
    private String medalName;

    /**
     * 徽章的图标
     */
    private String medalIcon;

    /**
     * 奖牌的描述
     */
    private String desc;

    /**
     * 徽章的数量
     */
    private int count;

    /**
     * 未知
     */
    private int point;
}
