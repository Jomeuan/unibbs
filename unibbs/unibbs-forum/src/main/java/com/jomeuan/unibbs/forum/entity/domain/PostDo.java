package com.jomeuan.unibbs.forum.entity.domain;

import java.time.LocalDateTime;

import com.jomeuan.unibbs.forum.bo.PostBo;
import com.jomeuan.unibbs.forum.entity.Action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 帖子,覆盖了 action 及其内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDo {
    private Long actionId;
    // 作者
    private Long userId;

    private Integer type;
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

    public Action getAction() {
        Action action = new Action(actionId, userId, targetId, type, contentId, time, Long.valueOf(0));
        return action;
    }

    public PostBo toPostBo() {
        return new PostBo(
                actionId,
                userId,
                Action.ActionType.of(type),
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
