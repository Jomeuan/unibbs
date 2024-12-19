package com.jomeuan.unibbs.service.impl;

import com.jomeuan.unibbs.entity.Comment;
import com.jomeuan.unibbs.mapper.CommentMapper;
import com.jomeuan.unibbs.service.ICommentService;
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
