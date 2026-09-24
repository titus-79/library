package co.simplon.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record BookRequestDto(

        @NotBlank(message = "Le titre est obligatoire")
        String title,

        @NotBlank(message = "L'auteur est obligatoire")
        String author,

        @NotEmpty(message = "Au moins une catégorie est requise")
        List<String> category,

        @Positive(message = "L'année de publication doit être positive")
        int yearPublish,

        @PositiveOrZero(message = "Le nombre d'exemplaires ne peut pas être négatif")
        int nbCopyAllowed
) {
}