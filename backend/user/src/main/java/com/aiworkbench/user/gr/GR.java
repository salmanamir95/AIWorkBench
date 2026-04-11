package com.aiworkbench.user.gr;

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

    // ===== SUCCESS RESPONSE =====
    public static <T> GR<T> success(T data) {
        return new GR<>(true, data, "Success");
    }

    public static <T> GR<T> success(T data, String message) {
        return new GR<>(true, data, message);
    }

    // ===== ERROR RESPONSE =====
    public static <T> GR<T> error(String message) {
        return new GR<>(false, null, message);
    }
}
