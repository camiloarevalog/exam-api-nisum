package com.exam.nisum.user.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa el modelado de phone.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Phone {

    private String number;
    private String citycode;
    private String countrycode;
    private String userId;
}
