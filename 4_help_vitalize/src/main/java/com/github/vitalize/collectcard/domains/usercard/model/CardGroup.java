package com.github.vitalize.collectcard.domains.usercard.model;

import lombok.Data;
import org.apache.commons.lang.BooleanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户卡组信息.
 *
 * @author yanghuan
 */
@Data
public class CardGroup {

    /**
     * 卡组ID.
     */
    private Long id;

    /**
     * 册内卡组及顺序.
     */
    private List<Card> cardList = new ArrayList<>();

    /**
     * 扩展信息（集齐状态、时间等）.
     */
    private ExtraInfo extraInfo;










    /**
     * 扩展信息
     * @author yanghuan
     */
    public static class ExtraInfo {

        /**
         * 是否已收集完成，卡牌全部集齐且全部激活
         */
        private Boolean isCollectCompleted;

        public boolean isCollectCompleted() {
            return BooleanUtils.isTrue(isCollectCompleted);
        }

        public void setCollectCompleted(Boolean collectCompleted) {
            isCollectCompleted = collectCompleted;
        }
    }
}
