package com.rockstarinc.RIecom.controller;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rockstarinc.RIecom.dto.AuthenticationRequest;
import com.rockstarinc.RIecom.dto.SignupRequest;
import com.rockstarinc.RIecom.dto.UserDto;
import com.rockstarinc.RIecom.entity.User;
import com.rockstarinc.RIecom.repository.UserRepository;
import com.rockstarinc.RIecom.services.auth.AuthService;
import com.rockstarinc.RIecom.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class AuthController {
    // Dependencias inyectadas a través del constructor generado por Lombok
    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // Constantes para el prefijo y el nombre del encabezado de autorización
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private final AuthService authService;
    // Endpoint para autenticar al usuario
    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                HttpServletResponse response) throws IOException, JSONException {
        // Intentar autenticar al usuario con las credenciales proporcionadas
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
            authenticationRequest.getPassword()));
        // Lanza una excepción si las credenciales son incorrectas
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password.");
        }
        // Cargar los detalles del usuario para el nombre de usuario proporcionado
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        // Buscar al usuario en el repositorio usando su correo electrónico
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        // Generar un token JWT para el usuario autenticado
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if(optionalUser.isPresent()){
            // Si el usuario está presente en la base de datos, escribir la información en la respuesta
            response.getWriter().write(new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .toString()
            );

            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, " + "X-Requested-With, Content-Type, Accept, X-Custom-header");
            // Agregar el token JWT al encabezado de la respuesta
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }

    }

    @PostMapping("/sign-up")
        public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
            // Verifica si ya existe un usuario con el e-mail proporcionado.
            if(authService.hasUserWithEmail(signupRequest.getEmail())){
                return new ResponseEntity<>("User already exist", HttpStatus.NOT_ACCEPTABLE);
            }
            // Crea un nuevo usuario utilizando el servicio de autenticación.
            UserDto userDto = authService.createUser(signupRequest);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }
    
}
