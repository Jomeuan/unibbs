package com.jomeuan.unibbs.domain;

import lombok.Getter;

public enum PostMapperOrder {
    LIKE_COUNT("comment.likes_count"),
    COMMENT_COUNT("comment.comments_count"),
    COLLECTION_COUNT("comment.collections_count"),
    PULL_COUNT("comment.pull_count"),
    PUBLISH_TIME("action.time");

    private char[] value;

    PostMapperOrder(String s){
        this.value = s.toCharArray();
    }

    public String getValue(){
        return new String(this.value);
    }
}
