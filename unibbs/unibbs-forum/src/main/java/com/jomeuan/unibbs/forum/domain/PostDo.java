package com.jomeuan.unibbs.forum.domain;

import org.springframework.util.Assert;

import com.jomeuan.unibbs.forum.entity.ActionPo;
import com.jomeuan.unibbs.forum.entity.CommentPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDo {
    private ActionPo action;
    private CommentPo comment;

}
