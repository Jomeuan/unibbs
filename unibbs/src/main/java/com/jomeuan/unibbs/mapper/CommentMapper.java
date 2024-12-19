package com.jomeuan.unibbs.mapper;

import com.jomeuan.unibbs.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
public interface CommentMapper extends BaseMapper<Comment> {
    @Update("update `comment` set likes_count=likes_count+#{oprand} where id=#{id}")
    Integer updateLikeCount(@Param("id")Long id,@Param("oprand")Integer operand);

}
