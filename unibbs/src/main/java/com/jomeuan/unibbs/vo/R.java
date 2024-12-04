package com.jomeuan.unibbs.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class R {
    R.CodeType code;
    String msg;
    Object data;

    public static R ok(Object data) {
        R r = new R();
        r.setCode(R.CodeType.SUCCESS);
        r.setData(data);
        return r;
    }

    public static R error(String msg) {
        R r = new R();
        r.setCode(R.CodeType.ERROR);
        r.setMsg(msg);
        return r;
    }

    static enum CodeType {

        SUCCESS("200000", "success"),
        ERROR("500000", "failure");

        final private String code;
        final private String description;

        CodeType(String code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
