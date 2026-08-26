package co.simplon.library.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "book")
@NoArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;

    @Nonnull
    @Column(nullable = false)
    public String title;
//    un auteur
    @Nonnull
    @Column(nullable = false)
    public String Autor;
//    une catégorie
    @ElementCollection
    @CollectionTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    @Nonnull
    private List<String> category;
//    une année de publication
    @Nonnull
    @Column(nullable = false)
    public int yearPublish;
//    un nombre d'exemplaires disponibles
    @Nonnull
    @Column(nullable = false)
    public int nbCopyAllowed;


}
