package com.github.content.ugc.app;

import com.github.content.ugc.domain.UgcDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ugc内容业务处理.
 *
 * @author yanghuan
 */
@Service
public class UgcService {

    @Autowired
    private UgcDomainService ugcDomainService;

}
