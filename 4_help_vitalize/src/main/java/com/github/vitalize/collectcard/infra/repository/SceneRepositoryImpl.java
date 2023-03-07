package com.github.vitalize.collectcard.infra.repository;

import com.github.vitalize.collectcard.app.scene.repo.SceneRepository;
import com.github.vitalize.collectcard.app.scene.repo.model.SceneDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场景信息存储实现.
 *
 * @author yanghuan
 */
@Service
public class SceneRepositoryImpl implements SceneRepository {

    @Override
    public List<SceneDO> queryAllSceneList() {
        return null;
    }
}
