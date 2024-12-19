package com.jomeuan.unibbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @Note
 * 1.
 * id, user_id, target_id, type, content_id
   0 , 0,       0,         0,    NULL
 * 该条记录是所有发文(即不是评论的用户发言)的target
 * 2.
 * type为like时其contentId实际点赞的次数,奇数表示点赞了,偶数表示取消了点赞
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Getter
@Setter
public class Action implements Serializable {

  private static final long serialVersionUID = 1L;

  // action type
  @AllArgsConstructor
  @Getter
  public static enum ActionType {
  /**
   * id为1的action是所有发文的targetId
   */
    SUPER_COMMENT(0),

    COMMENT(1),

    /**
   * 逻辑上也是一种Comment,特指已经过时(被修改过)的comment,查找有效的comment时不用查多次表
   * 其target_id也是指向被评论的对象,content_id指向过去评论的内容
   */
    COMMENT_COVERED(2),

    /**
   * 点赞
   * 
   * @Note 其contentId为点赞的次数奇数表示点赞了,偶数表示取消了点赞
   */
    LIKE(3),

    COLLECT(4),

    PULL(5);

    int value;
    public static ActionType of(int value){
      for(ActionType at: ActionType.values()){
        if(at.value == value){
          return at;
        }
      }
      return null;
    }
    
  }

  /**********action fields define****************/

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long targetId;

    private Integer type;

    private Long contentId;

    private LocalDateTime time;

    private Long visibilityId;

    static enum ActionType {
        SUPPER_COMMENT,
        COMMENT,
        MODIFY,
        LIKE,
        COLLECT,
        PULL;
    }
}
