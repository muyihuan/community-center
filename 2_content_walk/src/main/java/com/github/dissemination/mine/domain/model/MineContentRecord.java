package com.github.dissemination.mine.domain.model;

import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.dissemination.mine.domain.enums.PrivilegeType;
import lombok.Data;

/**
 * 个人页动态信息.
 *
 * @author yanghuan
 */
@Data
public class MineContentRecord {

    /**
     * 谁的个人页.
     */
    private String uid;

    /**
     * 内容载体类型.
     */
    private ContentCarrierTypeEnum contentCarrierType;

    /**
     * 载体ID.
     */
    private Long contentCarrierId;

    /**
     * 动态的可见性@see PrivilegeType.
     */
    private PrivilegeType privilege;
}
