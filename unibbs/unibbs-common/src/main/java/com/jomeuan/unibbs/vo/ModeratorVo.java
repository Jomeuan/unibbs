package com.jomeuan.unibbs.vo;

import java.util.List;

import org.springframework.context.annotation.Profile;

import com.jomeuan.unibbs.entity.ModeratorPo;
import com.jomeuan.unibbs.entity.ProfilePo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModeratorVo {
    private ModeratorPo moderator;
    private ProfilePo Profile;
}
