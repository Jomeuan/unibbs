package com.jomeuan.unibbs.bo;

import java.time.LocalDateTime;

import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.domain.PostDo;
import com.jomeuan.unibbs.vo.PostVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBo {
    private Long actionId;
    // 作者
    private Long userId;

    private Action.ActionType type;
    // 发布时间
    private LocalDateTime time;

    private Long contentId;
    private String content;

    private Long targetId;
    private String targetContent;

    private Long likesCount;
    private Long commentsCount;
    private Long collectionsCount;
    private Long pullCount;

    public PostBo(Action action) {
        this(
                action.getId(),
                action.getUserId(),
                Action.ActionType.of(action.getType()),
                action.getTime(),
                action.getContentId(),
                null,
                action.getTargetId(),
                null,
                null,
                null,
                null,
                null);
    }

    public PostDo toPostDo() {
        return new PostDo(
                actionId,
                userId,
                type.getValue(),
                time,
                contentId,
                content,
                targetId,
                targetContent,
                likesCount,
                commentsCount,
                collectionsCount,
                pullCount);
    }

}
