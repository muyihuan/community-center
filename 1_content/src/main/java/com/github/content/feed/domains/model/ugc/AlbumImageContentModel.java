package com.github.content.feed.domains.model.ugc;

import lombok.Data;

/**
 * 词卡信息
 * @author edz
 */
@Data
public class AlbumImageContentModel {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 小图地址
     */
    private String iconImg;

    /**
     * 大图地址
     */
    private String iconImgLarge;

    /**
     * 标志有可能是转发的1表示是转发别人的
     */
    private Integer forwardState;
}