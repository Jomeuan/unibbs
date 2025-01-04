package com.jomeuan.unibbs.service;

import java.util.List;

import com.jomeuan.unibbs.bo.PostBo;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.domain.PostDo;

public interface IPostService {
    
    public PostBo save(PostBo post);
    public List<PostBo> listPostsByUserId(Long userId);
    public List<PostBo> listCommentsOf(PostBo post);

    /**
     * 根据contentid装填其对应的content和XXX_counts到post中
     * @param post
     * @param contentId
     * @return
     */
    public PostBo fillContent(PostBo post,Long contentId);

    public PostBo fillTargetContent(PostBo post,Long targetActionId);
}
