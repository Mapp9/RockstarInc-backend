package com.rockstarinc.RIecom.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rockstarinc.RIecom.dto.SignupRequest;
import com.rockstarinc.RIecom.dto.UserDto;
import com.rockstarinc.RIecom.entity.Order;
import com.rockstarinc.RIecom.entity.User;
import com.rockstarinc.RIecom.enums.OrderStatus;
import com.rockstarinc.RIecom.enums.UserRole;
import com.rockstarinc.RIecom.repository.OrderRepository;
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
    
    @Autowired
    private OrderRepository orderRepository;

    @Override
    // Método para crear un nuevo usuario basado en los datos de SignupRequest.
    public UserDto createUser(SignupRequest signupRequest){
        // Crea una nueva instancia de la entidad 'user'.
        User user = new User();
        user.setEmail(signupRequest.getEmail()); // Establece el email del usuario.
        user.setName(signupRequest.getName()); // Establece el nombre del usuario.
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword())); // Encripta y establece la contraseña del usuario.
        user.setRole(UserRole.CUSTOMER); // Asigna el rol de 'CUSTOMER' al usuario.
        User createdUser = userRepository.save(user);


        Order order = new Order();
        order.setAmount(0L);
        order.setTotalAmount(0L);
        order.setDiscount(0L);
        order.setUser(createdUser);
        order.setOrderStatus(OrderStatus.Pending);
        orderRepository.save(order);
        
        // Guarda el nuevo usuario en la base de datos y obtiene la instancia creada.

        
        // Crea un DTO de usuario para devolver al cliente.
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId()); // Establece el ID del usuario creado.

        return userDto; // Devuelve el DTO del usuario creado.
    }

    @Override
    // Método para verificar si existe un usuario con un email específico.
    public Boolean hasUserWithEmail(String email){
        return userRepository.findFirstByEmail(email).isPresent();
    }

    // Método ejecutado después de la construcción del bean para crear una cuenta de administrador.
    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);

        // Si no existe una cuenta de administrador, crea una nueva.
        if(null == adminAccount){
            User user = new User();
            user.setEmail("admin@test.com"); // Establece el email del administrador.
            user.setName("admin"); // Establece el nombre del administrador.
            user.setRole(UserRole.ADMIN); // Asigna el rol de 'ADMIN'.
            user.setPassword(bCryptPasswordEncoder.encode("admin")); // Encripta y establece la contraseña del administrador.
            userRepository.save(user); //Guarda la cuenta de administrador en la base de datos.
        }
    }
}
