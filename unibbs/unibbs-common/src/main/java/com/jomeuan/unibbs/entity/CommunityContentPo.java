package com.jomeuan.unibbs.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("community_content")
public class CommunityContentPo implements Serializable{
    private static final long serialVersionUID = -1435464667710622609L;
    // 等同于action_id
    @TableId
    private Long id;
    private String title;
    private String avatar;
    private String introduction;
    private String rule;
}
