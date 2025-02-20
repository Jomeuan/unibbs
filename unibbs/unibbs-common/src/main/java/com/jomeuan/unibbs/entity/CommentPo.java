package com.jomeuan.unibbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class CommentPo implements Serializable {

    private static final long serialVersionUID = 8728203009294584426L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String content;

    private Integer likesCount;

    private Integer commentsCount;

    private Integer collectionsCount;

    private Integer pullCount;
}
