package com.jomeuan.unibbs.forum.vo;

import java.time.LocalDateTime;

import com.jomeuan.unibbs.forum.domain.ActionType;
import com.jomeuan.unibbs.forum.domain.PostDo;
import com.jomeuan.unibbs.forum.entity.ActionPo;
import com.jomeuan.unibbs.forum.entity.CommentPo;
import com.jomeuan.unibbs.forum.vo.PostVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {
    private PostDo thisPost;
    private PostDo targetPost;

}
