package com.jomeuan.unibbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jomeuan.unibbs.domain.ActionType;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("action")
public class ActionPo implements Serializable {

  private static final long serialVersionUID = 7967441106828761402L;

  /**********action fields define****************/

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long targetId;

    // @EnumValue
    private ActionType type;

    private Long contentId;

    private LocalDateTime time;

}
