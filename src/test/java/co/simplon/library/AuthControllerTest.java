package co.simplon.library;

import co.simplon.library.entity.RoleEntity;
import co.simplon.library.entity.UserEntity;
import co.simplon.library.repository.RoleRepository;
import co.simplon.library.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// Spring Boot 3.x : org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
// Spring Boot 4.x : org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtDecoder jwtDecoder;

    private RoleEntity roleUser;

    @BeforeEach
    void setUp() {
        // le rôle est normalement créé par DataInitializer ; save() est idempotent (id assigné)
        roleUser = roleRepository.save(new RoleEntity("ROLE_USER"));
    }

    // ---------- POST /api/auth/register ----------

    @Test
    @DisplayName("register : crée l'utilisateur, renvoie 200 et ne stocke pas le mot de passe en clair")
    void register_createsUserWithHashedPassword() throws Exception {
        String body = """
                {"username":"alice-test","email":"alice@test.fr","password":"Secret123!"}
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("alice-test"))
                .andExpect(jsonPath("$.email").value("alice@test.fr"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.authorities").doesNotExist());

        UserEntity saved = userRepository.findByUsername("alice-test").orElseThrow();
        assertThat(saved.getEmail()).isEqualTo("alice@test.fr");
        assertThat(saved.getPassword())
                .isNotEqualTo("Secret123!")
                .startsWith("$2"); // hash BCrypt
        assertThat(passwordEncoder.matches("Secret123!", saved.getPassword())).isTrue();
        assertThat(saved.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("register : nom d'utilisateur déjà pris → 409")
    void register_duplicateUsername_returnsConflict() throws Exception {
        userRepository.save(new UserEntity(null, "bob-test", "bob@test.fr",
                passwordEncoder.encode("Secret123!"), Set.of(roleUser)));

        String body = """
                {"username":"bob-test","email":"autre@test.fr","password":"Secret123!"}
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    // ---------- POST /api/auth/login ----------

    @Test
    @DisplayName("login : identifiants valides → un JWT est retourné")
    void login_validCredentials_returnsJwt() throws Exception {
        userRepository.save(new UserEntity(null, "carol-test", "carol@test.fr",
                passwordEncoder.encode("pass"), Set.of(roleUser)));

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"carol-test","password":"pass"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("carol-test"))
                .andReturn().getResponse().getContentAsString();

        String token = JsonPath.read(response, "$.token");
        Jwt jwt = jwtDecoder.decode(token); // vérifie aussi la signature
        assertThat(jwt.getSubject()).isEqualTo("carol-test");
        assertThat(jwt.getClaimAsString("scope")).contains("ROLE_USER");
    }

    // Ces deux tests renvoient 500 tant que GlobalExceptionHandler importe
    // org.apache.tomcat.websocket.AuthenticationException au lieu de
    // org.springframework.security.core.AuthenticationException.

    @Test
    @DisplayName("login : mauvais mot de passe → 401 et pas de token")
    void login_wrongPassword_isRejected() throws Exception {
        userRepository.save(new UserEntity(null, "dave-test", "dave@test.fr",
                passwordEncoder.encode("pass"), Set.of(roleUser)));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"dave-test","password":"mauvais"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    @DisplayName("login : utilisateur inconnu → 401")
    void login_unknownUser_isRejected() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"fantome","password":"pass"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("register : un client ne peut pas s'attribuer le rôle ADMIN")
    void register_ignoresAuthoritiesSentByClient() throws Exception {
        String body = """
            {"username":"mallory-test","email":"mallory@test.fr","password":"Secret123!",
             "authorities":[{"authority":"ROLE_ADMIN"}]}
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        UserEntity saved = userRepository.findByUsername("mallory-test").orElseThrow();
        assertThat(saved.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("register : email invalide → 400")
    void register_invalidEmail_returnsBadRequest() throws Exception {
        String body = """
            {"username":"bad-email","email":"pas-un-email","password":"Secret123!"}
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("register : mot de passe trop court → 400")
    void register_shortPassword_returnsBadRequest() throws Exception {
        String body = """
            {"username":"short-pw","email":"short@test.fr","password":"abc"}
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("register : email déjà utilisé → 409")
    void register_duplicateEmail_returnsConflict() throws Exception {
        userRepository.save(new UserEntity(null, "erin-test", "erin@test.fr",
                passwordEncoder.encode("pass"), Set.of(roleUser)));

        String body = """
            {"username":"autre-nom","email":"erin@test.fr","password":"Secret123!"}
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }
}
