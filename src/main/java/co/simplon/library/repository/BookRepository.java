package co.simplon.library.repository;

import co.simplon.library.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface BookRepository extends JpaRepository<BookEntity, UUID> {
    Optional<BookEntity> findBookEntityByTitle(String title);
}
