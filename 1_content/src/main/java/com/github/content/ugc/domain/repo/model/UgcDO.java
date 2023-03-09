package com.github.content.ugc.domain.repo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * ugc存储元信息.
 *
 * @author yanghuan
 */
@Getter
@Setter
public class UgcDO {

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
    private Integer ugcType;

    /**
     * 内容(JSON).
     */
    private String content;

    /**
     * 对应的载体类别.
     */
    private Integer sourceCarrierType;

    /**
     * 对应的载体ID.
     */
    private String sourceCarrierId;

    /**
     * 扩展信息.
     */
    private String extraInfo;

    /**
     * 创建时间.
     */
    private Date createTime;
}
