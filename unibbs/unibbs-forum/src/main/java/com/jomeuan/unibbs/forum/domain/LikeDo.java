package com.jomeuan.unibbs.forum.domain;

import org.springframework.util.Assert;

import com.jomeuan.unibbs.forum.entity.ActionPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDo {
    // 注意contentId为点赞的次数
    ActionPo action;

    public LikeDo setLikesCount(int count) {
        this.action.setContentId(Long.valueOf(count));
        return this;
    }


}
