package co.simplon.library.repository;

import co.simplon.library.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface BookRepository extends JpaRepository<BookEntity, String> {
    Optional<BookEntity> findBookEntityByTitle(String title);
}
