package com.github.interaction.comment.domain;

import com.github.interaction.comment.domain.enums.CommentStateEnum;
import com.github.interaction.comment.domain.enums.InteractionTypeEnum;
import com.github.interaction.comment.domain.model.CommentInteractionInfo;
import com.github.interaction.comment.domain.repo.CommentRepository;
import com.github.interaction.comment.domain.repo.model.CommentDO;
import com.github.interaction.comment.exception.CommentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评论热度处理（点赞等越多热度越高）.
 *
 * @author yanghuan
 */
@Service
public class CommentHeatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentHeatService.class);

    @Autowired
    private CommentRepository commentRepository;

    private static final String KEY_COMMENT_HEAT_CHANGE = "comment:heat:change:";

    /**
     * 计算热度.
     *
     * @param interactionInfo 互动信息.
     */
    public void computeHeatScore(CommentInteractionInfo interactionInfo) {
        if(interactionInfo ==null || interactionInfo.getCommentId() == null || interactionInfo.getCount() == null) {
            throw new CommentException();
        }

        if(interactionInfo.getCount() == 0) {
            return;
        }

        if(interactionInfo.getType() == InteractionTypeEnum.LIKE) {
            if(interactionInfo.getCount() > 0) {
                addHeatScore(interactionInfo.getCommentId(), interactionInfo.getCount());
            }
            else {
                decrHeatScore(interactionInfo.getCommentId(), - interactionInfo.getCount());
            }
        }

        LOGGER.info("act=computeHeatScore interactionInfo");
    }

    /**
     * 增加热度.
     */
    void addHeatScore(Long commentId, Long changeScore) {
        if(commentId ==null || changeScore == null || changeScore < 0) {
            throw new CommentException();
        }

        // lock
        try {
            CommentDO comment = commentRepository.getComment(commentId);
            if(comment == null || comment.getStatus() == CommentStateEnum.DEL.getCode()) {
                throw new CommentException();
            }

            commentRepository.updateHeatScore(commentId, changeScore);

            LOGGER.info("act=addCommentHeatScore commentId={} changeScore={}", commentId, changeScore);
        }
        finally {
            // unlock
        }
    }

    /**
     * 减少热度.
     */
    void decrHeatScore(Long commentId, Long changeScore) {
        if(commentId ==null || changeScore == null || changeScore < 0) {
            throw new CommentException();
        }

        // lock
        try {
            CommentDO comment = commentRepository.getComment(commentId);
            if(comment == null || comment.getStatus() == CommentStateEnum.DEL.getCode()) {
                throw new CommentException();
            }

            commentRepository.updateHeatScore(commentId, - changeScore);

            LOGGER.info("act=decrCommentHeatScore commentId={} changeScore={}", commentId, changeScore);
        }
        finally {
            // unlock
        }
    }
}
