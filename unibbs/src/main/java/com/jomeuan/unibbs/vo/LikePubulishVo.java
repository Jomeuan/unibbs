package com.jomeuan.unibbs.vo;

import java.time.LocalDateTime;

import com.jomeuan.unibbs.bo.Post;
import com.jomeuan.unibbs.entity.Action;

import lombok.Data;

/**
 * 专门用来提交点赞/取消点赞的vo
 */
@Data
public class LikePubulishVo {
    private Long actionId;
    // 作者
    private Long userId;

    // 发布时间
    private LocalDateTime time;

    // "like"表示点赞 "cancel"表示取消点赞
    private String content;

    // 被点赞的action的id;
    private Long targetId;
    // 被点赞的内容的id(如果被点赞的action没有内容为空)
    private Long targetContentId;
    private String targetContent;

    public Post toPostBo() {
        Post result = new Post(
                actionId, userId, Action.ActionType.LIKE, time,
                targetContentId, content, targetId, targetContent,
                null, null, null, null);
        return result;
    }
}