package com.jomeuan.unibbs.entity;

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

    //以下四条在逻辑上是缓存而不是Comment的属性
    private Long likesCount;
    private Long commentsCount;
    private Long collectionsCount;
    private Long pullCount;

    private Integer state;

    /**
     * 如果有值,则指向上一个版本的comment
     */
    private Long lastCommentId;
}
