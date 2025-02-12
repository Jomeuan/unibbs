package com.jomeuan.unibbs.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("moderator")
public class ModeratorPo implements Serializable{

    @TableId
    private Long id;

    // 等同于community_id , 和 community_action_id
    private Long communityId;

    private Long userId;

    private String title;
}
