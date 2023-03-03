package com.github.content.feed.app.model.create;

import com.github.content.feed.domain.enums.FeedContentTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 图片feed特有信息.
 *
 * @author yanghuan
 */
@Setter
@Getter
public class ImageFeedCreateBO extends AbstractFeedCreateBO {

    /**
     * 图片地址.
     */
    private List<String> imageUrlList;

    public ImageFeedCreateBO() {
        super(FeedContentTypeEnum.IMAGE);
    }
}