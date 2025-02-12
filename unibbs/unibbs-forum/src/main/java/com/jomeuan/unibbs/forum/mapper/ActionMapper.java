package com.jomeuan.unibbs.forum.mapper;

import com.jomeuan.unibbs.domain.PostMapperOrder;
import com.jomeuan.unibbs.entity.ActionPo;
import com.jomeuan.unibbs.vo.PostVo;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ActionMapper extends BaseMapper<ActionPo> {

        /**
         * 找到所有评论 actionId对应帖子 的comment,而后组装成post
         * 
         * @param actionId
         * @param orderKey
         * @param isAsc
         * @param page     注意从0开始
         * @param limit
         * @return 注意返回的post.targetContent(actionId对应帖子的内容)==null
         */
        default List<PostVo> selectCommentPostByTargetAction(Long actionId,
                        PostMapperOrder orderKey,
                        Boolean isAsc,
                        Integer page,
                        Integer limit) {
                List<PostVo> posts = doSelectCommentPostByTargetActionId(
                                actionId,
                                orderKey.getValue(),
                                isAsc ? "asc" : "desc",
                                page * limit, limit);
                return posts;
        }

        /**
         * 找到所有评论 actionId对应帖子 的comment,而后组装成 post,从第offset个开始,返回limit个
         * 
         * @param orderKey    : comment.likes_count comment.comments_count
         *                    comment.collections_count comment.pull_count action.time之一
         * @param orderString : asc desc 之一
         * 
         * @return 注意返回的post.targetContent(actionId对应帖子的内容)==null
         */
        @Select("""
                        select action.id as aid,comment.id as cid , action.* , comment.*
                        from `action` inner join `comment`
                        on action.content_id=comment.id
                        where action.type=1 and action.target_id=#{actionId}
                        order by #{orderKey} #{orderString}
                        limit #{offset},#{limit}
                        """)
        @Results(id = "PostResultMap2", value = {
                        @Result(property = "actionId", column = "action.id"),
                        @Result(property = "type", column = "action.type"),
                        // @Result(property = "type", column = "action.type", typeHandler =
                        // EnumOrdinalTypeHandler.class),
                        @Result(property = "time", column = "action.time"),
                        @Result(property = "userId", column = "action.user_id"),
                        @Result(property = "targetId", column = "action.target_id"),
                        @Result(property = "contentId", column = "action.content_id"),
                        @Result(property = "content", column = "comment.contnet"),
                        @Result(property = "likesCount", column = "comment.likes_count"),
                        @Result(property = "commentsCount", column = "comment.comments_count"),
                        @Result(property = "collectionsCount", column = "comment.collections_count"),
                        @Result(property = "pullCount", column = "comment.pull_count")
        })
        List<PostVo> doSelectCommentPostByTargetActionId(
                        Long actionId,
                        String orderKey,
                        String orderString,
                        Integer offset,
                        Integer limit);

}
