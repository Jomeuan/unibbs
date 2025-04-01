package com.jomeuan.unibbs.entity;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("thread")
public class ThreadPo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8153760626064132971L;
    @TableId 
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long creatorId;
    private String title;
    private String avatar;
    private String introduction;

    
}
