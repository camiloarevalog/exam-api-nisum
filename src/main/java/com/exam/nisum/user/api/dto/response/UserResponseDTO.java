package com.exam.nisum.user.api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Clase DTO, encargada de tener la respuesta con la información
 *  del user, y contiene información saliente con campos adicionales.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private List<PhoneResponseDTO> phones;
    private LocalDate created;
    private LocalDate modified;
    private LocalDate lastLogin;
    private String token;
    private Boolean isActive;
}
