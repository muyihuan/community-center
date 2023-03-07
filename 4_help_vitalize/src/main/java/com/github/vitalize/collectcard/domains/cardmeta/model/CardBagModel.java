package com.github.vitalize.collectcard.domains.cardmeta.model;

import com.github.vitalize.collectcard.domains.cardmeta.enums.CardBagLevelEnum;
import com.github.vitalize.collectcard.domains.cardmeta.enums.CardBagStateEnum;
import com.github.vitalize.collectcard.infra.util.RandomByWeightUtil;
import com.github.vitalize.collectcard.infra.util.model.RandomItem;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡包实体.
 *
 * @author yanghuan
 */
@Data
public class CardBagModel {

    /**
     * 卡包的ID.
     */
    private Long id;

    /**
     * 卡包的名称.
     */
    private String name;

    /**
     * 卡包的等级.
     */
    private CardBagLevelEnum level;

    /**
     * 包内卡牌和卡牌权重.
     */
    private List<List<BagInfo>> bagCardsList;

    /**
     * 状态.
     */
    private CardBagStateEnum status;

    /**
     * 通过权重随机获取卡牌.
     */
    public List<Long> randomCardByWeight() {
        if(CollectionUtils.isEmpty(bagCardsList)) {
            return null;
        }

        List<Long> result = new ArrayList<>(2);
        for(List<BagInfo> bagInfos : bagCardsList) {
            List<RandomItem> items = new ArrayList<>();
            for(BagInfo bagInfo : bagInfos) {
                RandomItem item = new RandomItem();
                item.setId(bagInfo.getCardId());
                item.setWeight(bagInfo.getWeight());
                items.add(item);
            }
            RandomItem item = RandomByWeightUtil.random(items);
            if(item != null) {
                result.add(item.getId());
            }
        }

        return result;
    }
}
