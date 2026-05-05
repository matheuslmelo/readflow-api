package com.readflow.api.dto.mapper;

import com.readflow.api.dto.BookCreateDTO;
import com.readflow.api.dto.BookResponseDTO;
import com.readflow.api.dto.BookUpdateDTO;
import com.readflow.api.entity.Book;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public static Book toEntity(@Valid BookCreateDTO dto){
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublishedYear(dto.getPublishedYear());
        book.setTotalPages(dto.getTotalPages());
        return book;
    }

    public static BookResponseDTO toResponse(Book book){
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setTotalPages(book.getTotalPages());
        return dto;
    }

    public static List<BookResponseDTO> toResponseList(List<Book> books) {
        return books.stream()
                .map(BookMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static void updateEntity(Book entity, BookUpdateDTO dto) {
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getAuthor() != null) {
            entity.setAuthor(dto.getAuthor());
        }
        if (dto.getPublishedYear() != null) {
            entity.setPublishedYear(dto.getPublishedYear());
        }
        if (dto.getTotalPages() != null) {
            entity.setTotalPages(dto.getTotalPages());
        }
    }
}