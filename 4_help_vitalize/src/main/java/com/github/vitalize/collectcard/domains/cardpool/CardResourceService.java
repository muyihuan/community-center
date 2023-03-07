package com.github.vitalize.collectcard.domains.cardpool;

import com.github.vitalize.collectcard.domains.cardmeta.CardDomainService;
import com.github.vitalize.collectcard.domains.cardmeta.model.CardBagModel;
import com.github.vitalize.collectcard.domains.cardpool.model.Resource;
import com.github.vitalize.collectcard.infra.util.RandomByWeightUtil;
import com.github.vitalize.collectcard.infra.util.model.RandomItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡牌资源管理.
 *
 * @author yanghuan
 */
@Service
public class CardResourceService {

    @Autowired
    private CardDomainService cardDomainService;

    /**
     * 获取一个卡包资源
     */
    public CardBagModel getCardBag(String sceneKey) {
        List<Resource> probabilityInfos = getResourcePool(sceneKey);
        if(CollectionUtils.isEmpty(probabilityInfos)) {
            return null;
        }

        List<RandomItem> items = new ArrayList<>();
        for(Resource probability : probabilityInfos) {
            RandomItem item = new RandomItem();
            item.setId(probability.getId());
            item.setWeight(probability.getWeight());
            items.add(item);
        }

        RandomItem item = RandomByWeightUtil.random(items);
        if(item == null) {
            return null;
        }

       return cardDomainService.getCardTag(item.getId());
    }

    /**
     * 获取资源列表
     */
    private List<Resource> getResourcePool(String sceneKey) {
        return null;
    }
}
