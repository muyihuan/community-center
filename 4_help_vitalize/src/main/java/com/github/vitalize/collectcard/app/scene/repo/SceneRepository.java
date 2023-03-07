package com.github.vitalize.collectcard.app.scene.repo;

import com.github.vitalize.collectcard.app.scene.repo.model.SceneDO;

import java.util.List;

/**
 * 场景信息存储.
 *
 * @author yanghuan
 */
public interface SceneRepository {

    /**
     * 获取所有场景.
     *
     * @return
     */
    List<SceneDO> queryAllSceneList();
}
