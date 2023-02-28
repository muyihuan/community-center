package com.github.content.feed.app.checker.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 音频检查
 * @createTime 2021年05月24日
 */
@Service
public class AudioTypeChecker implements Checker {

    @Autowired
    private FeedTrackFacade feedTrackFacade;

    @Override
    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        // todo 加个上下文传下来一个流程共用一个
        FeedGradeConfig feedGradeConfig = ApolloConfigUtil.getFeedGradeConfig();
        List<String> whiteUserIdList = feedGradeConfig.getWhiteUserId();
        if (feedGradeConfig.getCanPublishAudio() == 0) {
            //获取限制的白名单，没有的话 全量关  有的话打开
            if (CollectionUtils.isEmpty(whiteUserIdList) || whiteUserIdList.contains(feedCreateModel.getUid())) {
                feedTrackFacade.trackPublishFeedFail(feedCreateModel, "maintaining");
                return CheckResult.fail("平台维护中，暂时无法发布语音");
            }
        }

        AudioFeedCreateBO audioFeedCreateBO = (AudioFeedCreateBO) feedCreateModel;
        if(StringUtils.isEmpty(audioFeedCreateBO.getAudioUrl())) {
            feedTrackFacade.trackPublishFeedFail(feedCreateModel, "audio_null");
            return CheckResult.fail("语音数据为空，请重新编辑后发送");
        }

        return CheckResult.success();
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.AUDIO;
    }

    @Override
    public int order() {
        return 1;
    }
}
