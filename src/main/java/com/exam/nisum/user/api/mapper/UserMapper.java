package com.exam.nisum.user.api.mapper;

import com.exam.nisum.user.api.entity.PhoneEntity;
import com.exam.nisum.user.api.entity.UserEntity;
import com.exam.nisum.user.api.model.Phone;
import com.exam.nisum.user.api.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Clase usada para mapear modelos a entidades y viciversa de user y phone.
 */

@Component
public class UserMapper {

    /**
     * Convierte una UserEntity en un User (Modelo de usuario)
     *
     * @param userEntity La entidad de usuario a convertir.
     * @return User respectivo modelo de usuario.
     */
    public static User toUserModel(UserEntity userEntity) {

        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .phones(userEntity.getPhones().stream().map(phoneEntity ->
                                Phone.builder()
                                        .number(phoneEntity.getNumber())
                                        .citycode(phoneEntity.getCityCode())
                                        .countrycode(phoneEntity.getCityCode()).build())
                        .collect(Collectors.toList()))
                .created(userEntity.getCreated())
                .modified(userEntity.getModified())
                .lastLogin(userEntity.getLastLogin())
                .isActive(userEntity.getIsActive() == null ? Boolean.FALSE : Boolean.TRUE)
                .token(userEntity.getToken())
                .build();

    }

    /**
     * Convierte un User en un UserEntity
     *
     * @param user El modelo de usuario a convertir.
     * @return UserEntity respectiva entidad de usuario.
     */
    public static UserEntity toUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setCreated(user.getCreated());
        userEntity.setModified(user.getModified());
        userEntity.setLastLogin(user.getLastLogin());
        userEntity.setToken(user.getToken());
        userEntity.setIsActive(user.getIsActive() == null ? Boolean.TRUE : Boolean.FALSE);
        return userEntity;
    }


    /**
     * Convierte un phone en un PhoneEntity
     *
     * @param phone El modelo de phone a convertir.
     * @return PhoneEntity respectiva entidad de telefono.
     */
    public static PhoneEntity toPhoneEntity(Phone phone) {
        PhoneEntity phoneEntity = new PhoneEntity();
        phoneEntity.setNumber(phone.getNumber());
        phoneEntity.setCityCode(phone.getCitycode());
        phoneEntity.setCountryCode(phone.getCountrycode());
        UserEntity userEntity = new UserEntity();
        userEntity.setId(phone.getUserId());
        phoneEntity.setUser(userEntity);
        return phoneEntity;
    }
}
