package com.github.content.feed.app.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 创建处理器，可以对创建UGC前、创建FEED前、创建 FEED 后进行扩展
 * @createTime 2021年05月10日 17:35:00
 */
@Component
public class CreateProcessorManager {

    @Autowired
    private List<CreateProcessor> createProcessors;

    @PostConstruct
    public void init() {
        createProcessors.sort(Comparator.comparingInt(CreateProcessor::order));
    }

    public void beforeCreateUgc(AbstractFeedCreateBO feedCreateModel) {
        for (CreateProcessor createProcessor : createProcessors) {
            if (createProcessor.match(feedCreateModel)) {
                createProcessor.beforeCreateUgc(feedCreateModel);
            }
        }
    }

    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateModel) {
        for (CreateProcessor createProcessor : createProcessors) {
            if (createProcessor.match(feedCreateModel)) {
                createProcessor.beforeCreateFeed(feedCreateModel);
            }
        }
    }

    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateModel) {
        for (CreateProcessor createProcessor : createProcessors) {
            if (createProcessor.match(feedCreateModel)) {
                createProcessor.afterCreateFeed(feedId, feedCreateModel);
            }
        }
    }
}
