package com.jomeuan.unibbs.forum.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jomeuan.unibbs.forum.entity.domain.PostDo;

public interface PostMapper {
    /**
     * 根据 targetId 查找action对应的所有评论comment及其内容
     */
    List<PostDo> selectPostByActionId(
        @Param("actionId") Long actionId,
        @Param("key")String key,
        @Param("isAsc")boolean isAsc
    );
}
