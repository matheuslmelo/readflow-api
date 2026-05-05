package com.readflow.api.service;

import com.readflow.api.dto.BookCreateDTO;
import com.readflow.api.dto.BookUpdateDTO;
import com.readflow.api.dto.mapper.BookMapper;
import com.readflow.api.entity.Book;
import com.readflow.api.exception.global.exceptions.BookNotFoundException;
import com.readflow.api.repository.BookRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: "+id));
    }

    public List<Book> findByTitle(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Book create(@Valid BookCreateDTO requestDTO) {
        return bookRepository.save(BookMapper.toEntity(requestDTO));
    }

    @Transactional
    public Book update(Long id, BookUpdateDTO requestDTO) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: "+id));
        BookMapper.updateEntity(existing,requestDTO);
        return existing;
    }

    public void delete(Long id){
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
        bookRepository.deleteById(id);
    }
}