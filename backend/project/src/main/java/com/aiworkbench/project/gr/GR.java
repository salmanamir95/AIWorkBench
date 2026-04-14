package com.aiworkbench.project.gr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GR<T> {

    private boolean success;
    private T data;
    private String message;

    public static <T> GR<T> success(T data) {
        return new GR<>(true, data, "Success");
    }

    public static <T> GR<T> success(T data, String message) {
        return new GR<>(true, data, message);
    }

    public static <T> GR<T> error(String message) {
        return new GR<>(false, null, message);
    }
}
