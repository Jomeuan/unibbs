package com.jomeuan.unibbs.forum.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jomeuan.unibbs.forum.domain.ActionType;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("action")
public class ActionPo implements Serializable {

  private static final long serialVersionUID = 1L;

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
