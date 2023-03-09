package com.github.content.ugc.domain;

import com.github.content.feed.exception.FeedException;
import com.github.content.ugc.domain.enums.ContentCarrierTypeEnum;
import com.github.content.ugc.domain.enums.UgcTypeEnum;
import com.github.content.ugc.domain.model.*;
import org.apache.commons.lang.StringUtils;
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
        if(StringUtils.isEmpty(uid) || content == null) {
            throw new FeedException();
        }

        Long id = 0L;
        UgcTypeEnum ugcType = content.getUgcType();
        if(content.getUgcType() == UgcTypeEnum.TEXT) {
            Text text = (Text) content;
            return id;
        }
        else if(ugcType == UgcTypeEnum.IMAGE) {
            Image image = (Image) content;
            return id;
        }
        else if(ugcType == UgcTypeEnum.AUDIO) {
            Audio audio = (Audio) content;
            return id;
        }
        else if(ugcType == UgcTypeEnum.VIDEO) {
            Video video = (Video) content;
            return id;
        }
        else {
            throw new FeedException();
        }
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
