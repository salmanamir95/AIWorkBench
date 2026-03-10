package com.example.Auth.api.response;

public record GenericResponse<T>(
        boolean success,
        T data,
        String msg
) {
    public static <T> GenericResponse<T> success(final T data, final String msg) {
        return new GenericResponse<>(true, data, msg);
    }

    public static GenericResponse<Void> success(final String msg) {
        return new GenericResponse<>(true, null, msg);
    }

    public static GenericResponse<Void> failure(final String msg) {
        return new GenericResponse<>(false, null, msg);
    }
}
