package com.jomeuan.unibbs.forum.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jomeuan.unibbs.forum.entity.CommentPo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
public interface CommentMapper extends BaseMapper<CommentPo> {
    @Update("update `comment` set likes_count=likes_count+#{oprand} where id=#{id}")
    Integer updateLikeCount(@Param("id") Long id, @Param("oprand") Integer operand);

}
