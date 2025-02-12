package com.jomeuan.unibbs.entity;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("thread")
public class ThreadPo implements Serializable {

    @TableId 
    private Long id;
    private Long creatorId;
    private String title;
    private String avatar;
    private String introduction;

    
}
