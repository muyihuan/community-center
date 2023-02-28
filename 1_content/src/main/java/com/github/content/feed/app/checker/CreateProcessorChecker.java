package com.github.content.feed.app.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * @author yanghuan
 * @version 1.0.0
 * @Description 创建处理器，可以对创建UGC前、创建FEED前、创建 FEED 后进行扩展
 * @createTime 2021年05月10日 17:35:00
 */
@Service
public class CreateProcessorChecker {

    @Autowired
    private List<Checker> checkerList;

    @PostConstruct
    public void init() {
        checkerList.sort(Comparator.comparingInt(Checker::order));
    }

    public CheckResult check(AbstractFeedCreateBO feedCreateModel) {
        for (Checker checker : checkerList) {
            if (checker.matchContentType() == null || checker.matchContentType() == feedCreateModel.getContentType()) {
                CheckResult check = checker.check(feedCreateModel);
                if (!check.getSuccess()) {
                    return check;
                }
            }
        }

        return CheckResult.success();
    }
}
