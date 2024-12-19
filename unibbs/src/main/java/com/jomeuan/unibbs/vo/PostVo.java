package com.jomeuan.unibbs.vo;

import java.time.LocalDateTime;

import com.jomeuan.unibbs.bo.Post;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.Action.ActionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostVo {
    private Long actionId;
    // 作者
    private Long userId;
    /**
     * type define {@link com.jomeuan.unibbs.entity.Action.ActionType }不区分大小写
     */
    private String type;

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

    public PostVo(Post post) {
        this(
            post.getActionId(),
            post.getUserId(),
            post.getType().name(),
            post.getTime(),
            post.getContentId(),
            post.getContent(),
            post.getTargetId(),
            post.getTargetContent(),
            post.getLikesCount(),
            post.getCommentsCount(),
            post.getCollectionsCount(),
            post.getPullCount()
        );
    }

    public Post toPostBo() {
        Post res = new Post(
                actionId,
                userId,
                ActionType.valueOf(type.toUpperCase()),
                time==null?LocalDateTime.now():time,
                contentId,
                content,
                targetId,
                targetContent,
                likesCount,
                commentsCount,
                collectionsCount,
                pullCount);
        return res;
    }
}
