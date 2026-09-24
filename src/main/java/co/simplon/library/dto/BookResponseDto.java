package co.simplon.library.dto;

import java.util.List;
import java.util.UUID;

public record BookResponseDto(
        UUID id,
        String title,
        String author,
        List<String> category,
        int yearPublish,
        int nbCopyAllowed
) {

    public static BookResponseDto fromEntity(co.simplon.library.entity.BookEntity entity) {
        return new BookResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getCategory(),
                entity.getYearPublish(),
                entity.getNbCopyAllowed()
        );
    }
}