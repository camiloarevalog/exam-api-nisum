package com.exam.nisum.user.api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase DTO, Encargada de tener los datos del phone.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PhoneResponseDTO {

    private String number;
    private String citycode;
    private String countrycode;
}
