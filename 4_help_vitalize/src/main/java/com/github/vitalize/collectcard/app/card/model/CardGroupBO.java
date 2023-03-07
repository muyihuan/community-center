package com.github.vitalize.collectcard.app.card.model;

import com.github.vitalize.collectcard.app.card.enums.CardGroupCollectStateEnum;
import lombok.Data;
import org.apache.commons.lang.BooleanUtils;

import java.util.List;

/**
 * 卡组实体.
 *
 * @author yanghuan
 */
@Data
public class CardGroupBO {

    /**
     * 卡组的ID
     */
    private Long id;

    /**
     * 卡组的名称
     */
    private String name;

    /**
     * 封面图
     */
    private String coverImg;

    /**
     * 集齐状态
     */
    private CardGroupCollectStateEnum state;

    /**
     * 组内卡牌
     */
    private List<CardBO> cardList;

    /**
     * 扩展信息
     */
    private ExtraInfo extraInfo;




    /**
     * 扩展信息
     * @author yanghuan
     */
    public static class ExtraInfo {

        /**
         * 是否已集齐
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
