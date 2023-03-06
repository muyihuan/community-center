package com.github.interaction.comment.infra;

import com.github.interaction.comment.exception.CommentException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * mq消息发送
 * @author yanghuan
 */
@Service
public class MqProducer {

    /**
     * 发送mq信息
     */
    public void sendMsg(String topic, String value) {
        if(StringUtils.isEmpty(topic) || StringUtils.isEmpty(value)) {
            throw new CommentException();
        }

    }
}
