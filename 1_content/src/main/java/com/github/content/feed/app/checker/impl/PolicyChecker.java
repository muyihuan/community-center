package com.github.content.feed.app.checker.impl;

import com.github.content.feed.app.audit.AuditService;
import com.github.content.feed.app.checker.CheckResult;
import com.github.content.feed.app.checker.Checker;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 政策相关校验
 * @author yanbghuan
 */
@Service
public class PolicyChecker implements Checker {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    public static final String SEVENTH_ANNIVERSARY = "玩吧7周年生日庆典邀请函";

    @Autowired
    private UserGroupFacade userGroupFacade;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        SceneInfoBO sceneInfoBO = feedCreateModel.getSceneInfoBO();
        if(sceneInfoBO != null && sceneInfoBO.getScene() == FeedScene.ACT_SCENE && StringUtils.isNotEmpty(sceneInfoBO.getSceneParam())) {
            Map sceneParamMap = WbJSON.fromJson(sceneInfoBO.getSceneParam(), Map.class);
            boolean check = MapUtils.isNotEmpty(sceneParamMap) && StringUtils.equals(SEVENTH_ANNIVERSARY, MapUtils.getString(sceneParamMap, "actSceneName", ""));
            if(check) {
                return CheckResult.success();
            }
        }

        if(!SpecialPolicyConfig.checkWithinTime(feedCreateModel.getUid())) {
            return CheckResult.success();
        }

        InGroupStatusEnum inGroupStatus = userGroupFacade.checkUserBelongGroup(feedCreateModel.getUid(), SpecialPolicyConfig.get().getGroupId());
        if(inGroupStatus != InGroupStatusEnum.IN) {
            LOGGER.info("act=doPolicyChecker in feedCreateModel={}", WbJSON.toJson(feedCreateModel));
            return CheckResult.fail("系统升级中，该功能预计2022.06.07日恢复");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return null;
    }

    @Override
    public int order() {
        return 2;
    }
}
