package com.github.vitalize.collectcard.infra.util;

import com.github.vitalize.collectcard.infra.util.model.RandomItem;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 通过权重随机抽取.
 *
 * @author yanghuan
 */
public class RandomByWeightUtil {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private static final Long START = 1L;

    /**
     * 通过权重随机抽取.
     */
    public static RandomItem random(List<RandomItem> items) {
        long sum = items.stream().mapToLong(RandomItem::getWeight).sum();
        if (sum == 0) {
            return null;
        }

        // 先打乱.
        Collections.shuffle(items, RANDOM);

        // 随机一个数.
        long hit = RANDOM.nextLong(START, sum + 1);

        // 找到命中者.
        long start = START;
        long end;
        for (RandomItem item : items) {
            end = start + item.getWeight() - 1;

            if(start <= hit && hit <= end) {
                return item;
            }

            start = end + 1;
        }

        return null;
    }
}
