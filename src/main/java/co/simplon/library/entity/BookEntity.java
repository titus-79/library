package co.simplon.library.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Nonnull
    @Column(nullable = false)
    private String title;
//    un auteur
    @Nonnull
    @Column(nullable = false)
    private String author;
//    une catégorie
    @ElementCollection
    @CollectionTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    @Nonnull
    private List<String> category;
//    une année de publication
    @Column(nullable = false)
    private int yearPublish;
//    un nombre d'exemplaires disponibles
    @Column(nullable = false)
    private int nbCopyAllowed;


}
