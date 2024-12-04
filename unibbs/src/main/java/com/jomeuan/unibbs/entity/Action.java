package com.jomeuan.unibbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @Note
 * 1.
 * id, user_id, target_id, type, content_id
   0 , 0,       0,         0,    NULL
 * 该条记录是所有发文(即不是评论的用户发言)的target
 * 2.
 * type为like时其contentId实际点赞的次数,奇数表示点赞了,偶数表示取消了点赞
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Getter
@Setter
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long targetId;

    private Integer type;

    private Long contentId;

    private LocalDateTime time;

    private Long visibilityId;

    static enum ActionType {
        SUPPER_COMMENT,
        COMMENT,
        MODIFY,
        LIKE,
        COLLECT,
        PULL;
    }
}
