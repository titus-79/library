package co.simplon.library.config;

import co.simplon.library.exception.SecurityConfigurationException;
import co.simplon.library.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig (AuthService authServiceInjected) {
        this.authService = authServiceInjected;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            return http
                    // CSRF désactivé : authentification stateless par JWT (Authorization header),
                    // aucun cookie de session n'est utilisé, donc pas de risque CSRF classique.
                    .csrf(csrf -> csrf.disable())
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/api/auth/**").permitAll()
                            .anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 ->
                            oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                    .build();
        } catch (Exception e) {
            throw new SecurityConfigurationException("Échec de la configuration de la chaîne de sécurité", e);
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        try {
            AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authManagerBuilder.userDetailsService(authService);
            return authManagerBuilder.build();
        } catch (Exception e) {
            throw new SecurityConfigurationException("Échec de la construction de l'AuthenticationManager", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Nécessaire pour l'authentification pour les JWT exprime à Spring security quel service il doit utiliser pour authentifier l'utilisateur
    // une fois déclaré on a le droit de faire une injection de dépendance dans le authcontroller pour la route login


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix(""); // on désactive l'ajout automatique de SCOPE_
        authoritiesConverter.setAuthoritiesClaimName("scope"); // explicite, même si "scope" est déjà cherché par défaut

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtConverter;
    }

}
