package com.jomeuan.unibbs.forum.mapper;

import com.jomeuan.unibbs.forum.entity.Action;
import com.jomeuan.unibbs.forum.entity.domain.PostDo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
public interface ActionMapper extends BaseMapper<Action> {
    /**
     * 根据 targetId 查找action对应的所有评论comment及其内容
     */
    List<PostDo> selectPostByActionId(
            @Param("actionId") Long actionId,
            @Param("key") String key,
            @Param("isAsc") boolean isAsc);

    // TODO: 后续需要加入userId查询功能
    // List<Post> selectPostByUserId(
    // @Param("actionId") Long userId,
    // @Param("key")String key,
    // @Param("isAsc")boolean isAsc);

}
