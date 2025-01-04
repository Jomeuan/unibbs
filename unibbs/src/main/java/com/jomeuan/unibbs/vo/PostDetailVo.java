package com.jomeuan.unibbs.vo;

import java.util.List;

import com.jomeuan.unibbs.bo.PostBo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailVo {
    private PostVo postVo;
    //只有第一级的评论是list(显示很多个),第二级为贴主的回复
    private List<PostDetailVo.Comment> comments;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Comment{
        private PostVo postVo;
        //对postvo的评论
        private PostDetailVo.Comment comment;
    }
}
