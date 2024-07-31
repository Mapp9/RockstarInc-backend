package com.rockstarinc.RIecom.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    // Nombre de usuario que se usará para autenticar al usuario
    private String username;
    // Contraseña asociada al nombre de usuario para el proceso de autenticación
    private String password;

}
