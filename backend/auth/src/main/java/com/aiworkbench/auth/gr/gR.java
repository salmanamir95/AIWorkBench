package com.aiworkbench.auth.gr;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
public class gR<T> {
    private T data;
    private boolean success;
    private String msg;
    private LocalDateTime time = LocalDateTime.now();

    public static <T> gR<T> success(T data, String msg) {
        gR<T> response = new gR<>();
        response.setData(data);
        response.setSuccess(true);
        response.setMsg(msg);
        return response;
    }

    public static <T> gR<T> failure(String msg) {
        gR<T> response = new gR<>();
        response.setSuccess(false);
        response.setMsg(msg);
        return response;
    }
}