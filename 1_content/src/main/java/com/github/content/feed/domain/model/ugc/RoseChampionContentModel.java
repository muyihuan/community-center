package com.github.content.feed.domain.model.ugc;

import lombok.Data;

/**
 * 奖牌信息
 * @author edz
 */
@Data
public class RoseChampionContentModel {

    /**
     * 类别，0日榜 1周榜
     */
    private int type;

    /**
     * 花魁uid
     */
    private String championUid;

    /**
     * 花魁的花数
     */
    private int championRoseCount;

    /**
     * 花魁的用户名
     */
    private String championUserName;

    /**
     * 花魁的头像
     */
    private String championIconImg;

    /**
     * 花魁的大头像
     */
    private String championIconImgLarge;

    /**
     * 性别
     */
    private String championGender;

    /**
     * 用户签名
     */
    private String championSignature;

    /**
     * 描述
     */
    private String desc;
}
