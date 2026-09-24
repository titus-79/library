package co.simplon.library.controller;

import co.simplon.library.dto.LoginDto;
import co.simplon.library.dto.LoginRequest;
import co.simplon.library.dto.RegisterRequest;
import co.simplon.library.dto.RegisterResponse;
import co.simplon.library.entity.RoleEntity;
import co.simplon.library.entity.UserEntity;
import co.simplon.library.exception.ResourceNotFoundException;
import co.simplon.library.exception.UserAlreadyExistsException;
import co.simplon.library.repository.RoleRepository;
import co.simplon.library.repository.UserRepository;
import co.simplon.library.service.AuthService;
import co.simplon.library.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public AuthController(
            PasswordEncoder passwordEncoderInjected,
            UserRepository userRepositoryInjected,
            AuthService authServiceInjected,
            AuthenticationManager authManagerInjected,
            TokenService tokenServiceInjected,
            RoleRepository roleRepositoryInjected) {
        this.passwordEncoder = passwordEncoderInjected;
        this.userRepository = userRepositoryInjected;
        this.authService = authServiceInjected;
        this.authManager = authManagerInjected;
        this.tokenService = tokenServiceInjected;
        this.roleRepository = roleRepositoryInjected;
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request) {
        if (authService.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "Un utilisateur avec le nom '" + request.username() + "' existe déjà");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "Un utilisateur avec l'email '" + request.email() + "' existe déjà");
        }

        RoleEntity roleUser = roleRepository.findById("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Rôle ROLE_USER introuvable en base"));

        String rawPassword = Objects.requireNonNull(request.password(), "password ne doit pas être null");

        String encodedPassword = Objects.requireNonNull(
                passwordEncoder.encode(rawPassword),
                "le mot de passe encodé ne doit pas être null");

        UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(encodedPassword)
                .authorities(Set.of(roleUser))
                .build();

        UserEntity saved = userRepository.save(user);
        return new RegisterResponse(saved.getId(), saved.getUsername(), saved.getEmail());
    }

    @PostMapping("/login")
    public LoginDto login(@Valid @RequestBody LoginRequest request) {
        String username = Objects.requireNonNull(request.username(), "username ne doit pas être null");
        String password = Objects.requireNonNull(request.password(), "password ne doit pas être null");

        Authentication auth = this.authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        String token = tokenService.generateToken(auth);

        return new LoginDto(token, auth.getName());
    }
}