package co.simplon.library.init;

import co.simplon.library.entity.BookEntity;
import co.simplon.library.entity.RoleEntity;
import co.simplon.library.entity.UserEntity;
import co.simplon.library.repository.BookRepository;
import co.simplon.library.repository.RoleRepository;
import co.simplon.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final String GENRE_ROMAN = "Roman";
    private static final String GENRE_CLASSIQUE = "Classique";
    private static final String GENRE_SCIENCE_FICTION = "Science-fiction";
    private static final String GENRE_DYSTOPIE = "Dystopie";
    private static final String GENRE_FANTASY = "Fantasy";
    private static final String GENRE_AVENTURE = "Aventure";
    private static final String GENRE_PHILOSOPHIE = "Philosophie";
    private static final String GENRE_JEUNESSE = "Jeunesse";

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Externalisés (ex: application.properties / variables d'environnement)
    // plutôt qu'écrits en clair dans le code source.
    @Value("${app.seed.admin-password}")
    private String seedAdminPassword;

    @Value("${app.seed.user-password}")
    private String seedUserPassword;

    public DataInitializer(
            BookRepository bookRepositoryInjected,
            UserRepository userRepositoryInjected,
            RoleRepository roleRepositoryInjected,
            PasswordEncoder passwordEncoderInjected) {
        this.bookRepository = bookRepositoryInjected;
        this.userRepository = userRepositoryInjected;
        this.roleRepository = roleRepositoryInjected;
        this.passwordEncoder = passwordEncoderInjected;
    }

    @Override
    public void run(String... args) throws Exception {

        List<BookEntity> books = List.of(
                new BookEntity(null, "Les Misérables", "Victor Hugo", List.of(GENRE_ROMAN, GENRE_CLASSIQUE), 1862, 5),
                new BookEntity(null, "1984", "George Orwell", List.of(GENRE_SCIENCE_FICTION, GENRE_DYSTOPIE), 1949, 7),
                new BookEntity(null, "Le Petit Prince", "Antoine de Saint-Exupéry", List.of("Conte", GENRE_JEUNESSE), 1943, 10),
                new BookEntity(null, "Germinal", "Émile Zola", List.of(GENRE_ROMAN, GENRE_CLASSIQUE), 1885, 4),
                new BookEntity(null, "L'Étranger", "Albert Camus", List.of(GENRE_ROMAN, GENRE_PHILOSOPHIE), 1942, 6),
                new BookEntity(null, "Le Comte de Monte-Cristo", "Alexandre Dumas", List.of(GENRE_ROMAN, GENRE_AVENTURE), 1844, 3),
                new BookEntity(null, "Madame Bovary", "Gustave Flaubert", List.of(GENRE_ROMAN, GENRE_CLASSIQUE), 1857, 4),
                new BookEntity(null, "Dune", "Frank Herbert", List.of(GENRE_SCIENCE_FICTION), 1965, 8),
                new BookEntity(null, "Fahrenheit 451", "Ray Bradbury", List.of(GENRE_SCIENCE_FICTION, GENRE_DYSTOPIE), 1953, 6),
                new BookEntity(null, "Le Seigneur des Anneaux", "J.R.R. Tolkien", List.of(GENRE_FANTASY, GENRE_AVENTURE), 1954, 9),
                new BookEntity(null, "Harry Potter à l'école des sorciers", "J.K. Rowling", List.of(GENRE_FANTASY, GENRE_JEUNESSE), 1997, 12),
                new BookEntity(null, "Crime et Châtiment", "Fiodor Dostoïevski", List.of(GENRE_ROMAN, GENRE_CLASSIQUE), 1866, 3),
                new BookEntity(null, "La Peste", "Albert Camus", List.of(GENRE_ROMAN, GENRE_PHILOSOPHIE), 1947, 5),
                new BookEntity(null, "Le Rouge et le Noir", "Stendhal", List.of(GENRE_ROMAN, GENRE_CLASSIQUE), 1830, 2),
                new BookEntity(null, "Notre-Dame de Paris", "Victor Hugo", List.of(GENRE_ROMAN, GENRE_CLASSIQUE), 1831, 4),
                new BookEntity(null, "Le Meilleur des mondes", "Aldous Huxley", List.of(GENRE_SCIENCE_FICTION, GENRE_DYSTOPIE), 1932, 6),
                new BookEntity(null, "Vingt mille lieues sous les mers", "Jules Verne", List.of(GENRE_SCIENCE_FICTION, GENRE_AVENTURE), 1870, 7),
                new BookEntity(null, "Les Fleurs du mal", "Charles Baudelaire", List.of("Poésie", GENRE_CLASSIQUE), 1857, 3),
                new BookEntity(null, "L'Alchimiste", "Paulo Coelho", List.of(GENRE_ROMAN, GENRE_PHILOSOPHIE), 1988, 8),
                new BookEntity(null, "American Gods", "Neil Gaiman", List.of(GENRE_FANTASY, GENRE_ROMAN), 2001, 5)
        );

        this.bookRepository.saveAll(books);

        RoleEntity user = new RoleEntity("ROLE_USER");
        RoleEntity admin = new RoleEntity("ROLE_ADMIN");
        this.roleRepository.saveAll(List.of(admin, user));

        String encodedAdminPassword = Objects.requireNonNull(
                passwordEncoder.encode(seedAdminPassword), "Password encoding failed for admin seed user");
        String encodedUserPassword = Objects.requireNonNull(
                passwordEncoder.encode(seedUserPassword), "Password encoding failed for standard seed user");

        List<UserEntity> users = List.of(
                new UserEntity(null, "Jeff", "jeff.bezos@amazon.fr", encodedAdminPassword, Set.of(admin)),
                new UserEntity(null, "Homer", "homer.simpson@simpson.us", encodedUserPassword, Set.of(user))
        );

        this.userRepository.saveAll(users);
    }
}