package co.simplon.library;

import co.simplon.library.entity.BookEntity;
import co.simplon.library.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// Spring Boot 3.x : org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
// Spring Boot 4.x : org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // rollback automatique après chaque test
class BookSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    // Le JwtAuthenticationConverter de l'appli désactive le préfixe SCOPE_ :
    // les autorités sont donc "ROLE_USER" / "ROLE_ADMIN", pas "SCOPE_ADMIN".
    private static RequestPostProcessor asUser() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"));
    }

    private static RequestPostProcessor asAdmin() {
        return jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    private BookEntity saveBook() {
        return bookRepository.save(BookEntity.builder()
                .title("Livre de test")
                .author("Auteur de test")
                .category(List.of("Roman"))
                .yearPublish(2000)
                .nbCopyAllowed(2)
                .build());
    }

    private static final String BOOK_JSON = """
            {
              "title": "Nouveau titre",
              "author": "Nouvel auteur",
              "category": ["Essai"],
              "yearPublish": 2024,
              "nbCopyAllowed": 5
            }
            """;

    // ---------- Utilisateur USER ----------

    @Test
    @DisplayName("USER : peut consulter la liste des livres")
    void user_canListBooks() throws Exception {
        saveBook();

        mockMvc.perform(get("/api/books").with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("USER : peut consulter un livre par son id")
    void user_canGetBookById() throws Exception {
        BookEntity book = saveBook();

        mockMvc.perform(get("/api/books/{id}", book.getId()).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Livre de test"));
    }

    @Test
    @DisplayName("USER : ne peut pas supprimer un livre (403)")
    void user_cannotDeleteBook() throws Exception {
        BookEntity book = saveBook();

        mockMvc.perform(delete("/api/books/{id}", book.getId()).with(asUser()))
                .andExpect(status().isForbidden());

        assertThat(bookRepository.existsById(book.getId())).isTrue();
    }

    @Test
    @DisplayName("Sans token : accès refusé (401)")
    void anonymous_isUnauthorized() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    // ---------- Utilisateur ADMIN ----------

    @Test
    @DisplayName("ADMIN : peut créer un livre")
    void admin_canCreateBook() throws Exception {
        long before = bookRepository.count();

        mockMvc.perform(post("/api/books")
                        .with(asAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK_JSON))
                .andExpect(status().isOk()) // le contrôleur renvoie 200 (et non 201)
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Nouveau titre"));

        assertThat(bookRepository.count()).isEqualTo(before + 1);
    }

    @Test
    @DisplayName("USER : ne peut pas créer un livre (403)")
    void user_cannotCreateBook() throws Exception {
        mockMvc.perform(post("/api/books")
                        .with(asUser())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN : peut modifier un livre (sans créer de doublon)")
    void admin_canUpdateBook() throws Exception {
        BookEntity book = saveBook();
        long before = bookRepository.count();

        mockMvc.perform(put("/api/books/{id}", book.getId())
                        .with(asAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BOOK_JSON))
                .andExpect(status().isOk())
                // échoue tant que updateBook renvoie save(updateBook) au lieu de save(book)
                .andExpect(jsonPath("$.id").value(book.getId().toString()))
                .andExpect(jsonPath("$.title").value("Nouveau titre"));

        assertThat(bookRepository.count()).isEqualTo(before);
        assertThat(bookRepository.findById(book.getId()).orElseThrow().getAuthor())
                .isEqualTo("Nouvel auteur");
    }

    @Test
    @DisplayName("ADMIN : peut supprimer un livre")
    void admin_canDeleteBook() throws Exception {
        BookEntity book = saveBook();

        mockMvc.perform(delete("/api/books/{id}", book.getId()).with(asAdmin()))
                .andExpect(status().isNoContent());

        assertThat(bookRepository.existsById(book.getId())).isFalse();
    }
}
