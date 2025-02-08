package com.jomeuan.unibbs.forum.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionType {
    /**
     * 是帖子的载体,板块/吧就是一个 COMMUNITY
     * 用户在某个板块的发言,其action.targetId=这个板块的id
     */
    COMMUNITY(0),

    COMMENT_BROADCAST(6),

    COMMENT(1),

    /**
     * 逻辑上也是一种Comment,特指已经过时(被修改过)的comment,查找有效的comment时不用查多次表
     * 其target_id也是指向被评论的对象,content_id指向过去评论的内容
     */
    COMMENT_COVERED(2),

    /**
     * 点赞
     * 
     * @Note 其contentId为点赞的次数奇数表示点赞了,偶数表示取消了点赞
     */
    LIKE(3),

    COLLECT(4),

    THREAD(7),

    VIEW(5);

    @EnumValue
    int value;

    public static ActionType of(int value) {
        for (ActionType at : ActionType.values()) {
            if (at.value == value) {
                return at;
            }
        }
        return null;
    }

}