package co.simplon.library.init;

import co.simplon.library.entity.BookEntity;
import co.simplon.library.entity.RoleEntity;
import co.simplon.library.repository.BookRepository;
import co.simplon.library.repository.RoleRepository;
import co.simplon.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DataInitializer(
            BookRepository bookRepositoryInjected,
            UserRepository userRepositoryInjected,
            RoleRepository roleRepositoryInjected) {
        this.bookRepository = bookRepositoryInjected;
        this.userRepository = userRepositoryInjected;
        this.roleRepository = roleRepositoryInjected;
    }

    @Override
    public void run(String... args) throws Exception {

        List<BookEntity> books = List.of(
                new BookEntity(null, "Les Misérables", "Victor Hugo", List.of("Roman", "Classique"), 1862, 5),
                new BookEntity(null, "1984", "George Orwell", List.of("Science-fiction", "Dystopie"), 1949, 7),
                new BookEntity(null, "Le Petit Prince", "Antoine de Saint-Exupéry", List.of("Conte", "Jeunesse"), 1943, 10),
                new BookEntity(null, "Germinal", "Émile Zola", List.of("Roman", "Classique"), 1885, 4),
                new BookEntity(null, "L'Étranger", "Albert Camus", List.of("Roman", "Philosophie"), 1942, 6),
                new BookEntity(null, "Le Comte de Monte-Cristo", "Alexandre Dumas", List.of("Roman", "Aventure"), 1844, 3),
                new BookEntity(null, "Madame Bovary", "Gustave Flaubert", List.of("Roman", "Classique"), 1857, 4),
                new BookEntity(null, "Dune", "Frank Herbert", List.of("Science-fiction"), 1965, 8),
                new BookEntity(null, "Fahrenheit 451", "Ray Bradbury", List.of("Science-fiction", "Dystopie"), 1953, 6),
                new BookEntity(null, "Le Seigneur des Anneaux", "J.R.R. Tolkien", List.of("Fantasy", "Aventure"), 1954, 9),
                new BookEntity(null, "Harry Potter à l'école des sorciers", "J.K. Rowling", List.of("Fantasy", "Jeunesse"), 1997, 12),
                new BookEntity(null, "Crime et Châtiment", "Fiodor Dostoïevski", List.of("Roman", "Classique"), 1866, 3),
                new BookEntity(null, "La Peste", "Albert Camus", List.of("Roman", "Philosophie"), 1947, 5),
                new BookEntity(null, "Le Rouge et le Noir", "Stendhal", List.of("Roman", "Classique"), 1830, 2),
                new BookEntity(null, "Notre-Dame de Paris", "Victor Hugo", List.of("Roman", "Classique"), 1831, 4),
                new BookEntity(null, "Le Meilleur des mondes", "Aldous Huxley", List.of("Science-fiction", "Dystopie"), 1932, 6),
                new BookEntity(null, "Vingt mille lieues sous les mers", "Jules Verne", List.of("Science-fiction", "Aventure"), 1870, 7),
                new BookEntity(null, "Les Fleurs du mal", "Charles Baudelaire", List.of("Poésie", "Classique"), 1857, 3),
                new BookEntity(null, "L'Alchimiste", "Paulo Coelho", List.of("Roman", "Philosophie"), 1988, 8),
                new BookEntity(null, "American Gods", "Neil Gaiman", List.of("Fantasy", "Roman"), 2001, 5)
        );

        this.bookRepository.saveAll(books);

        List<RoleEntity> roles = List.of(
                new RoleEntity("ROLE_USER"),
                new RoleEntity("ROLE_ADMIN")
        );

        this.roleRepository.saveAll(roles);
    }
}