package com.github.dissemination.moment.domain;

import com.github.content.feed.exception.FeedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 活跃朋友圈收集（朋友圈有feed进来，就认为是活跃的）.
 *
 * @author yanghuan
 */
@Service
public class ActiveMomentCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMomentCollector.class);

    private static final String ACTIVE_MOMENTS = "active:moments:list";

    /**
     * 收集获取的朋友圈
     */
    public void collect(String uid) {
        if(StringUtils.isEmpty(uid)) {
            return;
        }

    }

    /**
     * 获取并删除获取的朋友圈
     * @param count 拿取的数量
     * @return 活跃朋友圈列表
     */
    List<String> pop(Integer count) {
        if(count == null || count <= 0) {
            throw new FeedException();
        }

        return new ArrayList<>();
    }

    private String getCacheKey() {
        return ACTIVE_MOMENTS;
    }
}
