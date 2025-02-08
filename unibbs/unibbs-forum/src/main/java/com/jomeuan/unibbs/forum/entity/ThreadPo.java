package com.jomeuan.unibbs.forum.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("thread")
public class ThreadPo implements Serializable {

    @TableId 
    private Long id;
    private Long creatorId;
    private String title;
    private String avatar;
    private String introduction;

    
}
