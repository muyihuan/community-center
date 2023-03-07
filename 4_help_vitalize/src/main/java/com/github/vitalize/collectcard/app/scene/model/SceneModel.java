package com.github.vitalize.collectcard.app.scene.model;

import com.github.vitalize.collectcard.app.card.enums.SceneTypeEnum;
import com.github.vitalize.collectcard.app.scene.repo.model.SceneDO;
import lombok.Data;

import java.util.List;

/**
 * 场景信息实体.
 *
 * @author yanghuan
 */
@Data
public class SceneModel {

    /**
     * 场景唯一标识.
     */
    private String sceneKey;

    /**
     * 场景类型.
     */
    private SceneTypeEnum sceneType;

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

    public static SceneModel transformSceneDO(SceneDO sceneDO) {
        if(sceneDO == null) {
            return null;
        }

        SceneModel sceneModel = new SceneModel();
        sceneModel.setSceneKey(sceneDO.getSceneKey());
        sceneModel.setSceneType(SceneTypeEnum.getByCode(sceneDO.getSceneType()));
        sceneModel.setCardGroup(sceneDO.getCardGroup());
        sceneModel.setHomePage(sceneDO.getHomePage());
        sceneModel.setUnifyJump(sceneDO.getUnifyJump());
        return sceneModel;
    }
}
