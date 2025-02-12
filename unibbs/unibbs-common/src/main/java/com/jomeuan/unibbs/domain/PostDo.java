package com.jomeuan.unibbs.domain;

import org.springframework.util.Assert;

import com.jomeuan.unibbs.entity.ActionPo;
import com.jomeuan.unibbs.entity.CommentPo;

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
