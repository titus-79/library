package co.simplon.library.mapper;

import co.simplon.library.dto.BookRequestDto;
import co.simplon.library.entity.BookEntity;

public class BookMapper {

    private BookMapper() {
    }

    public static BookEntity toEntity(BookRequestDto dto) {
        return BookEntity.builder()
                .title(dto.title())
                .author(dto.author())
                .category(dto.category())
                .yearPublish(dto.yearPublish())
                .nbCopyAllowed(dto.nbCopyAllowed())
                .build();
    }

    public static void updateEntity(BookEntity entity, BookRequestDto dto) {
        entity.setTitle(dto.title());
        entity.setAuthor(dto.author());
        entity.setCategory(dto.category());
        entity.setYearPublish(dto.yearPublish());
        entity.setNbCopyAllowed(dto.nbCopyAllowed());
    }
}