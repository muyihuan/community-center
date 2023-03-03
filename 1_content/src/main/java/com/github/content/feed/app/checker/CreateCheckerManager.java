package com.github.content.feed.app.checker;

import com.github.content.feed.app.model.create.AbstractFeedCreateBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

/**
 * feed创建前置校验管理器.
 *
 * @author yanghuan
 */
@Service
public class CreateCheckerManager {

    @Autowired
    private List<Checker> checkerList;

    @PostConstruct
    public void init() {
        checkerList.sort(Comparator.comparingInt(Checker::order));
    }

    /**
     * feed创建前置校验.
     *
     * @param feedCreateBO feed创建信息.
     * @return 校验结果.
     */
    public CheckResult check(AbstractFeedCreateBO feedCreateBO) {
        for (Checker checker : checkerList) {
            if (checker.matchContentType() == null || checker.matchContentType() == feedCreateBO.getContentType()) {
                CheckResult check = checker.check(feedCreateBO);
                if (!check.getSuccess()) {
                    return check;
                }
            }
        }

        return CheckResult.success();
    }
}
