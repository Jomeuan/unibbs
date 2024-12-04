package com.jomeuan.unibbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Controller
@RequestMapping("/action")
public class ActionController {
    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
