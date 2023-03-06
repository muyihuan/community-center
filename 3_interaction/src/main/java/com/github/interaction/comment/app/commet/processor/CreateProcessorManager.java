package com.github.interaction.comment.app.commet.processor;

import com.github.interaction.comment.app.commet.model.AbstractCommentCreateBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * 管理维护处理器
 * @author yanghuan
 */
@Component
public class CreateProcessorManager {

    @Autowired
    private List<CreateProcessor> createProcessors;

    @PostConstruct
    private void init() {
        createProcessors.sort(Comparator.comparingInt(CreateProcessor::order));
    }

    public void beforeCreateComment(AbstractCommentCreateBO commentCreateBO) {
        for (CreateProcessor createProcessor : createProcessors) {
            createProcessor.beforeCreateComment(commentCreateBO);
        }
    }

    public void afterCreateComment(long feedId, AbstractCommentCreateBO commentCreateBO) {
        for (CreateProcessor createProcessor : createProcessors) {
            createProcessor.afterCreateComment(feedId, commentCreateBO);
        }
    }
}
