package com.aiworkbench.user.user.dto;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String email;

    private String password;

    private String name;

    private LocalDate dob;

    private Boolean verified;
    
}
