package com.exam.nisum.user.api.dto.request;


import com.exam.nisum.user.api.model.Phone;
import com.exam.nisum.user.api.model.User;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Clase DTO, encargada de tener la
 * información que se va usar en la petición entrante del usuario.
 */

@Data
public class UserRequestDTO {

    private String userId;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private List<Phone> phones;

    public User toModel() {
        return User.builder()
                .id(userId != null ? userId : null)
                .name(name)
                .email(email)
                .password(password)
                .phones(phones)
                .build();
    }
}
