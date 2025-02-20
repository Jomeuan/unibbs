package com.jomeuan.unibbs.event;

import org.springframework.context.ApplicationEvent;

import com.jomeuan.unibbs.domain.PostDo;
import com.jomeuan.unibbs.vo.PostVo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
public class PostPublishEvent extends ApplicationEvent{

    private final PostDo postDo;

    public PostPublishEvent(PostDo source) {
        super(source);
        this.postDo = source;
    }

}
