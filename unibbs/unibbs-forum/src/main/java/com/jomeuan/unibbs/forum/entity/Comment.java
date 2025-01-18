package com.jomeuan.unibbs.forum.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Getter
@Setter
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String content;

    private Long likesCount;

    private Long commentsCount;

    private Long collectionsCount;

    private Long pullCount;

    private Integer state;

    private Long lastCommentId;
}
