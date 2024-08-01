package com.rockstarinc.RIecom.dto;

import com.rockstarinc.RIecom.enums.UserRole;

import lombok.Data;

// Clase DTO (Data Transfer Object) que se utiliza para transferir información del usuario entre el cliente y el servidor.
@Data
public class UserDto {
    // Identificador único del usuario.
    private Long id;
    // E-mail del usuario.
    private String email;
    // Nombre del usuario.
    private String name;
    // Rol del usuario.
    private UserRole userRole;  
}
