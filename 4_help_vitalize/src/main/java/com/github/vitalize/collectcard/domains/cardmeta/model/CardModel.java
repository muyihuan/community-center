package com.github.vitalize.collectcard.domains.cardmeta.model;

import com.github.infrastructure.json.JsonMapper;
import com.github.vitalize.collectcard.domains.cardmeta.enums.CardLevelEnum;
import com.github.vitalize.collectcard.domains.cardmeta.enums.CardStateEnum;
import com.github.vitalize.collectcard.domains.cardmeta.repo.model.CardDO;
import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * 卡牌实体.
 *
 * @author yanghuan
 */
@Data
public class CardModel {

    /**
     * 卡牌的ID.
     */
    private Long id;

    /**
     * 卡牌的名称.
     */
    private String name;

    /**
     * 卡牌的等级.
     */
    private CardLevelEnum level;

    /**
     * 封面图.
     */
    private String coverImg;

    /**
     * 小图.
     */
    private String smallCoverImg;

    /**
     * 所属卡组的ID.
     */
    private Long cardGroupId;

    /**
     * 状态.
     */
    private CardStateEnum status;

    /**
     * 创建时间.
     */
    private Date createTime;

    public static CardModel transformCardDO(CardDO cardDO) {
        if(cardDO == null) {
            return null;
        }

        CardModel cardModel = new CardModel();
        cardModel.setId(cardDO.getId());
        cardModel.setName(cardDO.getName());
        cardModel.setLevel(CardLevelEnum.getByCode(cardDO.getLevel()));
        cardModel.setCoverImg(cardDO.getCoverImg());
        cardModel.setCardGroupId(cardDO.getCardGroupId());
        cardModel.setStatus(CardStateEnum.getByCode(cardDO.getStatus()));
        cardModel.setCreateTime(cardDO.getCreateTime());

        String extraInfo = cardDO.getExtraInfo();
        if(StringUtils.isNotEmpty(extraInfo)) {
            Map<String, Object> extraMap = JsonMapper.fromJson(extraInfo, Map.class);
            if(MapUtils.isNotEmpty(extraMap)) {
                cardModel.setSmallCoverImg(MapUtils.getString(extraMap, "smallCoverImg", ""));
            }
        }

        return cardModel;
    }
}
