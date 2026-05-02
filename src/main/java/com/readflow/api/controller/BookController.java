package com.readflow.api.controller;

import com.readflow.api.entity.Book;
import com.readflow.api.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>>getByTitle(@RequestParam String title) {
        List<Book> books = bookService.findByTitle(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book savedBook = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book book){
        return ResponseEntity.ok(bookService.update(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}