package co.simplon.library.controller;

import co.simplon.library.entity.UserEntity;
import co.simplon.library.exception.UserAlreadyExistsException;
import co.simplon.library.repository.UserRepository;
import co.simplon.library.service.AuthService;
import co.simplon.library.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthController(
            PasswordEncoder passwordEncoderInjected,
            UserRepository userRepositoryInjected,
            AuthService authServiceInjected,
            AuthenticationManager authManagerInjected,
            TokenService tokenServiceInjected) {
        this.passwordEncoder = passwordEncoderInjected;
        this.userRepository = userRepositoryInjected;
        this.authService = authServiceInjected;
        this.authManager = authManagerInjected;
        this.tokenService = tokenServiceInjected;
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

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserEntity user) {
        Authentication auth = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()));
        String token = tokenService.generateToken(auth);
        return Map.of("token", token);
    }
}