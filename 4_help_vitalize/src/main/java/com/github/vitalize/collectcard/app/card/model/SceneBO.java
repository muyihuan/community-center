package com.github.vitalize.collectcard.app.card.model;

import com.github.vitalize.collectcard.app.card.enums.SceneTypeEnum;
import lombok.Data;

/**
 * 场景信息.
 *
 * @author yanghuan
 */
@Data
public class SceneBO {

    /**
     * 场景唯一标识.
     */
    private String sceneKey;

    /**
     * 场景类型.
     */
    private SceneTypeEnum sceneType;

    /**
     * 统一跳转.
     */
    private String unifyJump;
}
