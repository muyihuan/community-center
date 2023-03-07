package com.github.vitalize.collectcard.app.scene.repo.model;

import lombok.Data;

import java.util.List;

/**
 * 场景信息实体.
 *
 * @author yanghuan
 */
@Data
public class SceneDO {

    /**
     * 场景唯一标识.
     */
    private String sceneKey;

    /**
     * 场景描述.
     */
    private String sceneDesc;

    /**
     * 场景类型.
     */
    private Integer sceneType;

    /**
     * 场景下的卡组列表.
     */
    private List<Long> cardGroup;

    /**
     * 场景欢迎页.
     */
    private String homePage;

    /**
     * 统一跳转.
     */
    private String unifyJump;
}
