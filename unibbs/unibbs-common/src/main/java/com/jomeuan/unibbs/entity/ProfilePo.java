package com.jomeuan.unibbs.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("profile")
public class ProfilePo implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -7397820696465066036L;
    /**
     * 等效于user.id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String nickname;

    // TODO:缺了个人简介
    
    private String avatar;
    private String phone;
    private String email;
    private LocalDateTime join_date;
    private LocalDateTime last_login_time;
    private LocalDateTime birthday;
    private Integer gender;
    private String country;
    private String address1;
    private String address2;
    private String address3;
    private String detail_address;
}
