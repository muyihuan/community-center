package com.github.dissemination.recommend.hotpool.score.impl;

import lombok.Getter;

/**
 * 评分器类型.
 *
 * @author yanghuan
 */
@Getter
public enum ScorerType {

    BASE_SCORE_SCORER(0, "基础分值评分"),
    INTERACTIVE_SCORER(1, "互动评分"),
    TIME_DECAY_SCORER(2, "热度时间衰减");

    private int code;
    private String desc;

    ScorerType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
