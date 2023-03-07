package com.github.vitalize.collectcard.domains.cardmeta.model;

import com.github.vitalize.collectcard.domains.cardmeta.enums.CardGroupStateEnum;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardGroupDO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 卡组实体.
 *
 * @author yanghuan
 */
@Data
public class CardGroupModel {

    /**
     * 卡组的ID.
     */
    private Long id;

    /**
     * 卡组的名称.
     */
    private String name;

    /**
     * 封面图.
     */
    private String coverImg;

    /**
     * 状态.
     */
    private CardGroupStateEnum status;

    /**
     * 组内卡牌.
     */
    private List<CardModel> cardList;

    /**
     * 创建时间.
     */
    private Date createTime;

    public static CardGroupModel transformCardDO(CardGroupDO cardGroupDO) {
        if(cardGroupDO == null) {
            return null;
        }

        CardGroupModel cardGroupModel = new CardGroupModel();
        cardGroupModel.setId(cardGroupDO.getId());
        cardGroupModel.setName(cardGroupDO.getName());
        cardGroupModel.setCoverImg(cardGroupDO.getCoverImg());
        cardGroupModel.setStatus(CardGroupStateEnum.getByCode(cardGroupDO.getStatus()));
        cardGroupModel.setCreateTime(cardGroupDO.getCreateTime());
        return cardGroupModel;
    }
}
