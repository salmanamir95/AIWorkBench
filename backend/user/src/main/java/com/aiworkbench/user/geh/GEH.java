package com.aiworkbench.user.geh;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aiworkbench.user.gr.GR;
import com.aiworkbench.user.user.dto.UserDTO;

@RestControllerAdvice
public class GEH {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GR<UserDTO>> handleRuntime(RuntimeException ex){
        return ResponseEntity
                    .badRequest()
                    .body(GR.error(ex.getMessage()));
    }
}
