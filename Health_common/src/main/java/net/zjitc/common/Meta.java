package net.zjitc.common;

import lombok.Data;


@Data
public class Meta {
    private String msg;
    private Integer status;

    public Meta(String msg, Integer status) {
        this.msg = msg;
        this.status = status;
    }


    public Meta() {
    }
}
