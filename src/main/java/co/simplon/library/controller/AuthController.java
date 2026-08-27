package co.simplon.library.controller;

import co.simplon.library.entity.UserEntity;
import co.simplon.library.exception.UserAlreadyExistsException;
import co.simplon.library.repository.UserRepository;
import co.simplon.library.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(
            PasswordEncoder passwordEncoderInjected,
            UserRepository userRepositoryInjected,
            AuthService authServiceInjected) {
        this.passwordEncoder = passwordEncoderInjected;
        this.userRepository = userRepositoryInjected;
        this.authService = authServiceInjected;
    }

    @PostMapping("/register")
    public UserEntity registerUser(@RequestBody UserEntity user) {
        if (authService.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Un utilisateur avec le nom '" + user.getUsername() + "' existe déjà"
            );
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
