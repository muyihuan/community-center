package com.github.content.ugc.domain.model;

import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.content.ugc.domain.enums.UgcTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Ugc实体.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class UgcModel {

    /**
     * 唯一ID.
     */
    private Long id;

    /**
     * 产生该内容的用户.
     */
    private String uid;

    /**
     * ugc内容类型(文字、图片、音频、视频).
     */
    private UgcTypeEnum ugcType;

    /**
     * 内容.
     */
    private Content content;

    /**
     * 对应的载体类别.
     */
    private ContentCarrierTypeEnum sourceCarrierType;

    /**
     * 对应的载体ID.
     */
    private String sourceCarrierId;

    /**
     * 扩展信息.
     */
    private Object extraInfo;

    /**
     * 创建时间.
     */
    private Date createTime;
}
