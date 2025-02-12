package com.jomeuan.unibbs.vo;

import com.jomeuan.unibbs.domain.PostDo;
import com.jomeuan.unibbs.vo.PostVo;

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
