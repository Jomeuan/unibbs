package com.jomeuan.unibbs.service.impl;

import com.jomeuan.unibbs.bo.Post;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.Comment;
import com.jomeuan.unibbs.entity.domain.PostDo;
import com.jomeuan.unibbs.mapper.ActionMapper;
import com.jomeuan.unibbs.service.IActionService;
import com.jomeuan.unibbs.service.ICommentService;
import com.jomeuan.unibbs.util.IdGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements IActionService {

   
    
   

}
