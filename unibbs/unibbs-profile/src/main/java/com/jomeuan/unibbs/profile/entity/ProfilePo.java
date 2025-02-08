package com.jomeuan.unibbs.profile.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("profile")
public class ProfilePo implements Serializable{
    // 等同于user_id
    @TableId
    private Long id;
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
    private LocalDateTime joinDate;
    private LocalDateTime lastLoginTime;
    private LocalDateTime birthday;
    private Integer gender;
    private String country;
    private String address1;
    private String address2;
    private String address3;
    private String detail_address;
}
