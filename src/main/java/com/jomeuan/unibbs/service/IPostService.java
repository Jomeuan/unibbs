package com.jomeuan.unibbs.service;

import java.util.List;

import com.jomeuan.unibbs.bo.Post;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.domain.PostDo;

public interface IPostService {
    
    public Post save(Post post);
    public List<Post> listPostsByUserId(Long userId);
    public List<Post> listCommentsOf(Post post);

    /**
     * 根据contentid装填其对应的content和XXX_counts到post中
     * @param post
     * @param contentId
     * @return
     */
    public Post fillContent(Post post,Long contentId);

    public Post fillTargetContent(Post post,Long targetActionId);
}
