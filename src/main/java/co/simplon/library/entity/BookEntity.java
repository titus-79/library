package co.simplon.library.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank
    @Column(nullable = false)
    private String title;
//    un auteur
    @NotBlank
    @Column(nullable = false)
    private String author;
//    une catégorie
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    @Nonnull
    private List<String> category;
//    une année de publication
    @Column(nullable = false)
    private int yearPublish;
//    un nombre d'exemplaires disponibles
    @PositiveOrZero
    @Column(nullable = false)
    private int nbCopyAllowed;


}
