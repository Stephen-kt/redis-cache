package com.ke.rediscache.http;

import java.io.Serializable;
import lombok.Data;

/**
 * @author stephen 2022/11/18 23:03
 */
@Data
public class ResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = -7809531983530219943L;

    private int code;
    private String msg;

    private T data;

    public ResponseEntity(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResponseEntity<T> ok(int code, String msg, T data) {
        return new ResponseEntity<>(code, msg, data);
    }

    public static <T> ResponseEntity<T> ok(int code, String msg) {
        return new ResponseEntity<>(code, msg);
    }
}
