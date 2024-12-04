package com.jomeuan.unibbs.vo;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAction {

    private Action action;
    private Comment comment;

}
