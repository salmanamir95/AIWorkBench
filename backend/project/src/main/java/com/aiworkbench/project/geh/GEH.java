package com.aiworkbench.project.geh;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aiworkbench.project.gr.GR;

@RestControllerAdvice
public class GEH {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GR<Void>> handleRuntime(RuntimeException ex){
        return ResponseEntity
                    .badRequest()
                    .body(GR.error(ex.getMessage()));
    }
}
