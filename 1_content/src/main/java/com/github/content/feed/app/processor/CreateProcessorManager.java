package com.github.content.feed.app.processor;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * feed创建流程管理器.
 *
 * @author yanghuan
 */
@Component
public class CreateProcessorManager {

    @Autowired
    private List<CreateProcessor> createProcessors;

    @PostConstruct
    public void init() {
        createProcessors.sort(Comparator.comparingInt(CreateProcessor::order));
    }

    public void beforeCreateFeed(AbstractFeedCreateBO feedCreateBO) {
        for (CreateProcessor createProcessor : createProcessors) {
            if (createProcessor.match(feedCreateBO)) {
                createProcessor.beforeCreateFeed(feedCreateBO);
            }
        }
    }

    public void afterCreateFeed(long feedId, AbstractFeedCreateBO feedCreateBO) {
        for (CreateProcessor createProcessor : createProcessors) {
            if (createProcessor.match(feedCreateBO)) {
                createProcessor.afterCreateFeed(feedId, feedCreateBO);
            }
        }
    }
}
