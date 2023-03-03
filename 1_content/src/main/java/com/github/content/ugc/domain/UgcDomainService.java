package com.github.content.ugc.domain;

import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.content.ugc.domain.model.Content;
import com.github.content.ugc.domain.model.UgcModel;
import org.springframework.stereotype.Service;

/**
 * ugc领域.
 *
 *                                     |
 *                                    V
 *     业务形态  -->  内容载体   -->  < ugc >  -->  (情感向|内容向)
 *
 * @author yanghuan
 */
@Service
public class UgcDomainService {

    /**
     * ugc创建.
     *
     * @return ugc的ID.
     */
    public Long createUgc(String uid, Content content, ContentCarrierTypeEnum carrierType, String carrierId) {
        return 0L;
    }

    /**
     * 通过ID获取ugc.
     *
     * @param id ugc的ID.
     * @return ugc的ID.
     */
    public UgcModel getUgc(Long id) {
        return null;
    }

    /**
     * 删除ugc.
     *
     * @param id ugc的ID.
     */
    public void deleteUgc(Long id) {

    }
}
