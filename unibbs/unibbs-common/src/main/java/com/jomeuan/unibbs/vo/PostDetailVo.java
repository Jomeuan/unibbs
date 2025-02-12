package com.jomeuan.unibbs.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailVo {
    private PostVo post;
    //只有第一级的评论是list(显示很多个),第二级为贴主的回复
    private List<PostDetailVo> comments;

}
