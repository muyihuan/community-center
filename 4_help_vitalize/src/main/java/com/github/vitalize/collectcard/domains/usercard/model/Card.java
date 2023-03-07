package com.github.vitalize.collectcard.domains.usercard.model;

import lombok.Data;
import org.apache.commons.lang.BooleanUtils;

/**
 * 用户卡牌.
 *
 * @author yanghuan
 */
@Data
public class Card {

    /**
     * 卡牌ID.
     */
    private Long id;

    /**
     * 卡牌数量.
     */
    private Long count;

    /**
     * 扩展信息.
     */
    private ExtraInfo extraInfo;











    /**
     * 扩展信息
     * @author yanghuan
     */
    public static class ExtraInfo {

        /**
         * 是否锁定
         */
        private Boolean isLock;

        public boolean isLock () {
            return BooleanUtils.isTrue(isLock);
        }

        public void setLock(Boolean lock) {
            isLock = lock;
        }
    }
}
