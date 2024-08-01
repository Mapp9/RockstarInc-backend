package com.rockstarinc.RIecom.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rockstarinc.RIecom.dto.SignupRequest;
import com.rockstarinc.RIecom.dto.UserDto;
import com.rockstarinc.RIecom.entity.user;
import com.rockstarinc.RIecom.enums.UserRole;
import com.rockstarinc.RIecom.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImpl implements AuthService {

    // Inyecta una instancia de UserRepository para interactuar con la base de datos.
    @Autowired
    private UserRepository userRepository;
    // Inyecta una instancia de BCryptPasswordEncoder para encriptar las contraseñas.
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;    
    // Método para crear un nuevo usuario basado en los datos de SignupRequest.
    public UserDto createUser(SignupRequest signupRequest){
        // Crea una nueva instancia de la entidad 'user'.
        user user = new user();
        user.setEmail(signupRequest.getEmail()); // Establece el email del usuario.
        user.setName(signupRequest.getName()); // Establece el nombre del usuario.
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword())); // Encripta y establece la contraseña del usuario.
        user.setRole(UserRole.CUSTOMER); // Asigna el rol de 'CUSTOMER' al usuario.
        // Guarda el nuevo usuario en la base de datos y obtiene la instancia creada.
        user createdUser = userRepository.save(user);

        
        // Crea un DTO de usuario para devolver al cliente.
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId()); // Establece el ID del usuario creado.

        return userDto; // Devuelve el DTO del usuario creado.
    }


    // Método para verificar si existe un usuario con un email específico.
    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }

    // Método ejecutado después de la construcción del bean para crear una cuenta de administrador.
    @PostConstruct
    public void createAdminAccount(){
        user adminAccount = userRepository.findByRole(UserRole.ADMIN);

        // Si no existe una cuenta de administrador, crea una nueva.
        if(null == adminAccount){
            user user = new user();
            user.setEmail("admin@test.com"); // Establece el email del administrador.
            user.setName("admin"); // Establece el nombre del administrador.
            user.setRole(UserRole.ADMIN); // Asigna el rol de 'ADMIN'.
            user.setPassword(new BCryptPasswordEncoder().encode("admin")); // Encripta y establece la contraseña del administrador.
            userRepository.save(user); //Guarda la cuenta de administrador en la base de datos.
        }
    }
}
