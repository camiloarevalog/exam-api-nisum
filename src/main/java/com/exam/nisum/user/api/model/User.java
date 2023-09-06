package com.exam.nisum.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase que representa el modelo user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
    private LocalDate created;
    private LocalDate modified = null;
    private LocalDate lastLogin;
    private String token;
    private Boolean isActive;
}
