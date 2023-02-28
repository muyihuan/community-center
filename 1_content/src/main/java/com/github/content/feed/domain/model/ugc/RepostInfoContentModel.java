package com.github.content.feed.domain.model.ugc;

import lombok.Data;

import java.util.LinkedList;
import java.util.Map;

/**
 * 转发UGC
 * @author lishipeng
 */
@Data
public class RepostInfoContentModel {

    /**
     * 根feed的ugc内容
     */
    private Map<String, Object> rootUgc;

    /**
     * 左转发链feedId
     */
    private LinkedList<Long> leftRepostChain;

    /**
     * 右转发链feedId
     */
    private LinkedList<Long> rightRepostChain;

    /**
     * 根feed的id
     */
    private Long rootFeedId;

    /**
     * 根feed的作者
     */
    private String rootFeedOwner;

    /**
     * 本feed的语音信息
     */
    private Map<String, Object> audio;

    /**
     * 本feed的图片信息
     */
    private Map<String, Object> image;

    /**
     * 本feed的ugc内容来源类型
     */
    private int repostSourceType;

    public boolean addFeedIdToChain(long sourceFeedId) {
        int maxLength4RightChain = 10;
        int maxLength4LeftChain = 10;

        if (rightRepostChain == null || rightRepostChain.size() == 0) {
            rightRepostChain = new LinkedList<>();
            rightRepostChain.add(sourceFeedId);
        } else if (rightRepostChain.size() < maxLength4RightChain) {
            rightRepostChain.add(0, sourceFeedId);
        } else {
            if (leftRepostChain == null || leftRepostChain.size() == 0) {
                leftRepostChain = new LinkedList<>();
            }
            leftRepostChain.add(0, sourceFeedId);
            while (leftRepostChain.size() > maxLength4LeftChain) {
                leftRepostChain.removeLast();
            }
        }
        return true;
    }
}
