package com.github.interaction.comment.infra;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * ip查询三方.
 *
 * @author yanghuan
 */
@Service
public class IpFacade {

    private static final String CHANE = "中国";

    /**
     * 获取用户ip
     */
    public String getUserIp(String uid) {
        return "";
    }

    /**
     * 查询ip所属地
     */
    public String getIpHome(String ip) {
        if(StringUtils.isBlank(ip)) {
            return "";
        }

        return "";
    }
}
