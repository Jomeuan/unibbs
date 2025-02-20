package com.jomeuan.unibbs.event;

import org.springframework.context.ApplicationEvent;

import com.jomeuan.unibbs.domain.PostDo;
import lombok.Getter;


@Getter
public class PostPublishEvent extends ApplicationEvent{

    private final PostDo postDo;

    public PostPublishEvent(PostDo source) {
        super(source);
        this.postDo = source;
    }

}
