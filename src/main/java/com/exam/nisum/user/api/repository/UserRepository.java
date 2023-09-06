package com.exam.nisum.user.api.repository;

import com.exam.nisum.user.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para gestionar la entidad UserEntity en la base de datos.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca un usuario por su email.
     *
     * @param email la dirección de correo electrónico del usuario a buscar
     * @return Optional<UserEntity> un Optional que contiene el UserEntity correspondiente.
     */
    Optional<UserEntity> findByEmail(String email);
}
