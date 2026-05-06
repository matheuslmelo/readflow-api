package com.readflow.api.service;

import com.readflow.api.dto.BookCreateDTO;
import com.readflow.api.dto.BookUpdateDTO;
import com.readflow.api.dto.mapper.BookMapper;
import com.readflow.api.entity.Book;
import com.readflow.api.exception.global.exceptions.BookNotFoundException;
import com.readflow.api.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
    }

    public List<Book> findByTitle(String title){
        log.debug("Searching books with title: {}", title != null ? title : "ALL");
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> findAll(){
        log.debug("Fetching all books");
        return bookRepository.findAll();
    }

    public Book create(BookCreateDTO requestDTO) {
        log.info("Creating new book with title: {}", requestDTO.getTitle());
        return bookRepository.save(BookMapper.toEntity(requestDTO));
    }

    @Transactional
    public Book update(Long id, BookUpdateDTO requestDTO) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
        log.info("Updating book with id: {}", id);
        BookMapper.updateEntity(existing,requestDTO);
        return existing;
    }

    public void delete(Long id){
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id: {}", id);
                    return new BookNotFoundException("Book not found with id: " + id);
                });
        log.info("Deleting book with id: {}", id);
        bookRepository.delete(existing);
    }
}