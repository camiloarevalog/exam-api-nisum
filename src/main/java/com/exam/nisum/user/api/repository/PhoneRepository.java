package com.exam.nisum.user.api.repository;

import com.exam.nisum.user.api.entity.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar la entidad PhoneEntity en la base de datos.
 */
public interface PhoneRepository extends JpaRepository<PhoneEntity, Long> {
}
