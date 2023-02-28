package com.github.content.ugc.domain;

import com.github.content.ugc.domain.model.UgcModel;

/**
 * ugc领域.
 *
 *                                     |
 *                                    V
 *     业务形态  -->  内容载体   -->  < ugc >  -->  (情感向|内容向)
 *
 * @author yanghuan
 */
public class UgcDomainService {

    /**
     * ugc创建.
     *
     * @return ugc的ID.
     */
    public Long createUgc(UgcModel ugcModel) {
        return 1L;
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
