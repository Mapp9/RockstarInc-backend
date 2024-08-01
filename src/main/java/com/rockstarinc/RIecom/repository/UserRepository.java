package com.rockstarinc.RIecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rockstarinc.RIecom.entity.user;
import com.rockstarinc.RIecom.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<user, Long>{

    // Método personalizado para buscar un usuario por su email
    Optional<user> findFirstByEmail(String email);
    // Método personalizado para buscar un usuario por su rol.
    user findByRole(UserRole userRole);
}
