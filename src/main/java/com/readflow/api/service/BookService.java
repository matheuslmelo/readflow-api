package com.readflow.api.service;

import com.readflow.api.entity.Book;
import com.readflow.api.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id ["+id+"]"));
    }

    public List<Book> findByTitle(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, Book book) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id ["+id+"]"));

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setPublishedYear(book.getPublishedYear());
        existing.setTotalPages(book.getTotalPages());

        return existing;
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }
}
