package com.exam.nisum.user.api.service;


import com.exam.nisum.user.api.dto.response.PhoneResponseDTO;
import com.exam.nisum.user.api.dto.response.UserResponseDTO;
import com.exam.nisum.user.api.entity.PhoneEntity;
import com.exam.nisum.user.api.entity.UserEntity;
import com.exam.nisum.user.api.mapper.UserMapper;
import com.exam.nisum.user.api.model.User;
import com.exam.nisum.user.api.repository.PhoneRepository;
import com.exam.nisum.user.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio respecto a los usuarios.
 */
@Service
@Transactional
public class UserService {
    //El formato del email debe ser (aaaaaaa@dominio.cl)
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // la contraseña debe tener minimo 8 y maximo 16 caracteres, números,
    // letras minúsculas y mayúsculas (123Acb1234*)
    public static final String PWD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$";

    private UserRepository userRepository;

    private PhoneRepository phoneRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PhoneRepository phoneRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Se obtiene la lista de todos los usuarios.
     *
     * @return Lista de usuarios.
     */
    public List<UserResponseDTO> getUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();

        return userEntityList.stream()
                .map(UserMapper::toUserModel)
                .map(this::setResponse)
                .collect(Collectors.toList());
    }


    /**
     * Guarda un nuevo usuario con una lista de telefonos.
     *
     * @param user Usuario a guardar.
     * @return UserResponseDTO DTO de respuesta con información del usuario guardado.
     */
    public UserResponseDTO saveUser(User user) {
        //Se valida que el email y contraseña cumplan con los formatos establecidos
        validateFormatEmail(user.getEmail());
        validatePassword(user.getPassword());

        // Guarda en User los campos que faltan por guardan
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(LocalDate.now());
        user.setLastLogin(user.getLastLogin() != null ? user.getLastLogin() : user.getCreated());
        user.setToken(UUID.randomUUID().toString());

        //Convertir el User(Modelo de usuario) a UserEntity para guardarlo en base de datos
        UserEntity userEntity = UserMapper.toUserEntity(user);
        userEntity.setPhones(user.getPhones().stream().map(phone -> {
                    PhoneEntity phoneEntity = UserMapper.toPhoneEntity(phone);
                    phoneEntity.setUser(userEntity);
                    return phoneEntity;
                }
        ).collect(Collectors.toList()));

        //Llama el metodo para guardar el UserEntity y devolver un userEntity
        UserEntity createdUser = userRepository.save(userEntity);

        //Convertir el UserEntity en User(Modelo de usuario)
        User createdUserResponse = UserMapper.toUserModel(createdUser);

        //Retorna una respuesta de User luego de invocar el metodo SetResponse
        return setResponse(createdUserResponse);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param user Usuario a actualizar.
     * @return UserResponseDTO DTO de respuesta con información del usuario actualizado.
     */
    public UserResponseDTO updateUser(User user) {

        //Se busca el usuario por email a ver si existe
        Optional<UserEntity> searchUser = userRepository.findByEmail(user.getEmail());

        if (searchUser.isEmpty()) {
            throw new IllegalArgumentException("No se encontro ningún usuario con el email" + user.getEmail());
        }

        // Actualiza los datos del usuario
        user.setId(searchUser.get().getId());
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPhones(user.getPhones());
        user.setCreated(searchUser.get().getCreated());
        user.setModified(LocalDate.now());
        user.setLastLogin(searchUser.get().getLastLogin());
        user.setToken(searchUser.get().getToken());

        UserEntity userEntity = UserMapper.toUserEntity(user);
        userEntity.setPhones(user.getPhones().stream().map(p -> {
            PhoneEntity phoneEntity = UserMapper.toPhoneEntity(p);
            phoneEntity.setUser(userEntity);
            return phoneEntity;
        }).collect(Collectors.toList()));

        // Guarda el usuario en la base de datos
        UserEntity modifiedUser = userRepository.save(userEntity);

        // Convierte el UserEntity a un User para poder retornarlo en el response
        User updatedUser = UserMapper.toUserModel(modifiedUser);

        // Arma y devuelve la respuesta
        return setResponse(updatedUser);


    }

    /**
     * Valida que el email tenga el formato correcto y no esté registrado previamente.
     *
     * @param email correo electrónico a validar
     * @throws ResponseStatusException si el email tiene un formato incorrecto o ya está registrado
     */
    private void validateFormatEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El formato del correo no es el indicado"
            );
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo " + email + " ya  registrado"
            );
        }
    }

    /**
     * Valida que la contraseña tenga el formato correcto.
     *
     * @param password contraseña a validar
     * @throws ResponseStatusException si la contraseña tiene un formato incorrecto
     */
    private void validatePassword(String password) {
        Pattern pattern = Pattern.compile(PWD_REGEX);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña tiene un formato incorrecto"
            );
        }
    }

    /**
     * Crea una respuesta de usuario a partir del modelo de usuario.
     *
     * @param user modelo de usuario
     * @return UserResponseDTO DTO de respuesta de usuario
     */
    private UserResponseDTO setResponse(User user) {
        List<PhoneResponseDTO> phones = new ArrayList<>();
        if (user.getPhones() != null) {
            user.getPhones().forEach(p -> {
                phones.add(PhoneResponseDTO.builder()
                        .number(p.getNumber())
                        .citycode(p.getCitycode())
                        .countrycode(p.getCountrycode())
                        .build());
            });
        }

        return UserResponseDTO.builder()
                .id(UUID.fromString(user.getId()))
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(phones)
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin() != null ? user.getLastLogin() : user.getCreated())
                .token(user.getToken())
                .isActive(user.getIsActive() != null && user.getIsActive())
                .build();
    }

}
