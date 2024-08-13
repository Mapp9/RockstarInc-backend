package com.rockstarinc.RIecom.services.jwt;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rockstarinc.RIecom.entity.User;
import com.rockstarinc.RIecom.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyección de dependencia para acceder al repositorio de usuarios
    @Autowired
    private UserRepository userRepository;
    // Método principal para cargar los detalles del usuario por su nombre de usuario (email)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca un usuario por su email en el repositorio
        Optional<User> optionalUser = userRepository.findFirstByEmail(username);
        // Si no se encuentra el usuario, se lanza una excepción
        if(optionalUser.isEmpty()) throw new UsernameNotFoundException("Username not found", null);
        // Si se encuentra el usuario, se retorna un objeto UserDetails con los detalles del usuario
        return new org.springframework.security.core.userdetails.User(
            optionalUser.get().getEmail(), //Nombre de usuario(email)
            optionalUser.get().getPassword(), //Contraseña
             new ArrayList<>()); // Lista de roles (vacía en este caso)
    }

}
