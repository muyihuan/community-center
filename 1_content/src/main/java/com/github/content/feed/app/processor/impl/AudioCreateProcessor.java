package com.github.content.feed.app.processor.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 音频创建处理
 * @createTime 2021年05月10日
 */
@Service
public class AudioCreateProcessor extends AbstractEmptyCreateProcessor {

    @Autowired
    private BubbleThirdRpc bubbleThirdRpc;

    @Override
    public void beforeCreateUgc(AbstractFeedCreateBO feedCreateModel) {
        AudioFeedCreateBO audioFeedCreateBO = (AudioFeedCreateBO) feedCreateModel;
        //获取用户气泡
        UserBubbleInfoBO userBubbleInfoBO = bubbleThirdRpc.getUserAudioPropId(feedCreateModel.getUid());
        if (userBubbleInfoBO != null) {
            AudioFeedCreateBO.ExtraInfo extraInfo = audioFeedCreateBO.getAudioExtraInfo();
            if(extraInfo != null) {
                extraInfo.setAudioPropId(userBubbleInfoBO.getGameBubbleId());
            }
            else {
                extraInfo = new AudioFeedCreateBO.ExtraInfo();
                extraInfo.setAudioPropId(userBubbleInfoBO.getGameBubbleId());
                audioFeedCreateBO.setAudioExtraInfo(extraInfo);
            }
        }
    }

    @Override
    public FeedContentTypeEnum matchContentType() {
        return FeedContentTypeEnum.AUDIO;
    }

    @Override
    public FeedSourceTypeEnum matchSourceType() {
        return FeedSourceTypeEnum.AUDIO;
    }

    @Override
    public int order() {
        return 0;
    }
}
