package com.github.dissemination.recommend.hotpool.score;

import com.github.content.ugc.domain.ContentCarrier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 评分结果.
 *
 * @author yanghuan
 */
@Getter
@Setter
@AllArgsConstructor
public class Result {

    /**
     * 用户.
     */
    private String user;

    /**
     * 内容载体.
     */
    private ContentCarrier contentCarrier;

    /**
     * 分数.
     */
    private Double score;
}
