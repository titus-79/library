package co.simplon.library.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.util.Arrays;

@Entity
@Table(name = "book")
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
    @Nonnull
    @Column(nullable = false)
    public String[] category;
//    une année de publication
    @Nonnull
    @Column(nullable = false)
    public int yearPublish;
//    un nombre d'exemplaires disponibles
    @Nonnull
    @Column(nullable = false)
    public int nbCopyAllowed;

    public BookEntity(String id, @Nonnull String title, @Nonnull String autor, @Nonnull String[] category, int yearPublish, int nbCopyAllowed) {
        this.id = id;
        this.title = title;
        Autor = autor;
        this.category = category;
        this.yearPublish = yearPublish;
        this.nbCopyAllowed = nbCopyAllowed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nonnull String title) {
        this.title = title;
    }

    @Nonnull
    public String getAutor() {
        return Autor;
    }

    public void setAutor(@Nonnull String autor) {
        Autor = autor;
    }

    @Nonnull
    public String[] getCategory() {
        return category;
    }

    public void setCategory(@Nonnull String[] category) {
        this.category = category;
    }

    public int getYearPublish() {
        return yearPublish;
    }

    public void setYearPublish(int yearPublish) {
        this.yearPublish = yearPublish;
    }

    public int getNbCopyAllowed() {
        return nbCopyAllowed;
    }

    public void setNbCopyAllowed(int nbCopyAllowed) {
        this.nbCopyAllowed = nbCopyAllowed;
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", Autor='" + Autor + '\'' +
                ", category=" + Arrays.toString(category) +
                ", yearPublish=" + yearPublish +
                ", nbCopyAllowed=" + nbCopyAllowed +
                '}';
    }
}
