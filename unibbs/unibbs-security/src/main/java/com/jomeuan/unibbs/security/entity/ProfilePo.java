package com.jomeuan.unibbs.security.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfilePo implements Serializable{
    /**
     * 等效于user.id
     */
    @TableId
    private Long id;
    private String nickname;
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
