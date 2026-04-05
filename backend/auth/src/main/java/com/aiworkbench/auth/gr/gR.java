package com.aiworkbench.auth.gr;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class gR<T> {
    private T data;
    private boolean success;
    private String msg;
    private LocalDateTime time=LocalDateTime.now();
}