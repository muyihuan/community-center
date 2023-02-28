package com.github.content.feed.infra;

import com.github.content.feed.exception.FeedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * mq.
 *
 * @author yanghuan
 */
@Service
public class MqProducer {

    /**
     * 发送mq信息
     */
    public void sendMsg(String topic, String value) {
        if(StringUtils.isEmpty(topic) || StringUtils.isEmpty(value)) {
            throw new FeedException("发 Mq 参数异常");
        }
    }
}
