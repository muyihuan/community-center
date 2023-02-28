package com.github.content.ugc.domain.model;

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
     * ugc类型(文字、图片、音频、视频、链接).
     */
    private Integer ugcType;

    /**
     * 内容.
     */
    private String content;

    /**
     * 对应的载体类别.
     */
    private Integer sourceCarrierType;

    /**
     * 对应的载体ID.
     */
    private String  sourceCarrierId;

    /**
     * 扩展信息.
     */
    private Object extraInfo;

    /**
     * 创建时间.
     */
    private Date createTime;
}
