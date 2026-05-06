package com.readflow.api.controller;

import com.readflow.api.dto.BookCreateDTO;
import com.readflow.api.dto.BookResponseDTO;
import com.readflow.api.dto.BookUpdateDTO;
import com.readflow.api.dto.mapper.BookMapper;
import com.readflow.api.entity.Book;
import com.readflow.api.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        log.debug("GET /books/{}", id);
        Book book = bookService.findById(id);
        return ResponseEntity.ok(BookMapper.toResponse(book));
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getBooks(@RequestParam(required = false) String title) {
        List<Book> books = null;
        if (title != null) {
            log.debug("GET /books?title={}", title);
            books = bookService.findByTitle(title);
        }else{
            log.debug("GET /books");
            books = bookService.findAll();
        }
        return ResponseEntity.ok(BookMapper.toResponseList(books));
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookCreateDTO requestDTO) {
        log.debug("POST /books", requestDTO.getTitle());
        Book savedBook = bookService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookMapper.toResponse(savedBook));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable Long id, @RequestBody BookUpdateDTO requestDTO){
        log.debug("PATCH /books/{}", id);
        Book book = bookService.update(id,requestDTO);
        return ResponseEntity.ok(BookMapper.toResponse(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("DELETE /books/{}", id);
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}