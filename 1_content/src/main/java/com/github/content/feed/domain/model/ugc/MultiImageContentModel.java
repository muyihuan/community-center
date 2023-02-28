package com.github.content.feed.domain.model.ugc;

import lombok.Data;

import java.util.List;

/**
 * 奖牌信息
 * @author lishipeng
 */
@Data
public class MultiImageContentModel {

    /**
     * 作者的uid
     */
    private String sourceUid;

    /**
     * 图片列表
     */
    private List<AlbumImageContentModel> imageList;
}
