package com.jomeuan.unibbs.forum.service.impl;

import com.jomeuan.unibbs.forum.entity.Comment;
import com.jomeuan.unibbs.forum.mapper.CommentMapper;
import com.jomeuan.unibbs.forum.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    
}
