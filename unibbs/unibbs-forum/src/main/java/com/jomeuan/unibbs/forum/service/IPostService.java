package com.jomeuan.unibbs.forum.service;

import java.util.List;

import com.jomeuan.unibbs.forum.bo.PostBo;
import com.jomeuan.unibbs.forum.entity.Action;
import com.jomeuan.unibbs.forum.entity.domain.PostDo;

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
