package com.rockstarinc.RIecom.services.auth;

import com.rockstarinc.RIecom.dto.SignupRequest;
import com.rockstarinc.RIecom.dto.UserDto;

// Interfaz que define los métodos de autenticación y gestión de usuarios.
public interface AuthService {


    // Método para crear un nuevo usuario.
    // Recibe un SignupRequest con la información necesaria para crear el usuario.
    // Retorna un UserDto con la información del usuario creado.
    UserDto createUser(SignupRequest signupRequest);

    // Método para verificar si existe un usuario con un email específico.
    // Recibe un email como parámetro.
    // Retorna true si existe un usuario con ese email, de lo contrario, retorna false.

    Boolean hasUserWithEmail(String email);
}
