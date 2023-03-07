package com.github.vitalize.collectcard.app.scene;

import com.github.vitalize.collectcard.app.scene.model.SceneModel;
import com.github.vitalize.collectcard.app.scene.repo.SceneRepository;
import com.github.vitalize.collectcard.app.scene.repo.model.SceneDO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 场景模块 （一期不开发，预留扩展）.
 *
 * @author yanghuan
 */
@Service
public class SceneService {

    @Autowired
    private SceneRepository sceneRepository;

    /**
     * 获取接入的所有场景.
     */
    public List<SceneModel> queryAllScenes() {
        List<SceneDO> sceneDOList = sceneRepository.queryAllSceneList();
        if(CollectionUtils.isEmpty(sceneDOList)) {
            return Collections.emptyList();
        }

        return sceneDOList.stream().map(SceneModel::transformSceneDO).collect(Collectors.toList());
    }

    /**
     * 通过场景标识获取场景信息.
     */
    public SceneModel getSceneModelByKey(String sceneKey) {
        List<SceneModel> sceneModelList = queryAllScenes();
        if(CollectionUtils.isEmpty(sceneModelList)) {
            return null;
        }

        Optional<SceneModel> optional = sceneModelList.stream().filter(sceneModel -> StringUtils.startsWith(sceneKey,sceneModel.getSceneKey())).findFirst();
        return optional.orElse(null);
    }
}
