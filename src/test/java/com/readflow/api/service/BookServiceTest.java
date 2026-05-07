package com.readflow.api.service;

import com.readflow.api.dto.BookCreateDTO;
import com.readflow.api.dto.BookUpdateDTO;
import com.readflow.api.dto.mapper.BookMapper;
import com.readflow.api.entity.Book;
import com.readflow.api.exception.global.exceptions.BookNotFoundException;
import com.readflow.api.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Nested
    class FindBookByIdTests {

        @DisplayName("Should return a book when book exists")
        @Test
        void shouldReturnBookWhenBookExists() {
            Long id = 1L;
            Book book = new Book();

            when(bookRepository.findById(id)).thenReturn(Optional.of(book));

            Book result = bookService.findById(id);
            assertSame(book, result);

            verify(bookRepository).findById(id);
        }

        @DisplayName("Should throw BookNotFoundException when a book does not exist")
        @Test
        void shouldThrowBookNotFoundExceptionWhenBookDoesNotExist() {
            Long id = 1L;

            when(bookRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(BookNotFoundException.class, () -> {
                bookService.findById(id);
            });

            verify(bookRepository).findById(id);
        }

    }

    @Nested
    class FindBooksByTitleTests {

        @DisplayName("Should return a list of books matching title")
        @Test
        void shouldReturnBooksMatchingTitle() {
            String title = "Harry Potter and the Prisoner of Askaban";
            Book book = new Book();
            book.setTitle(title);
            List<Book> books = List.of(book);

            when(bookRepository.findByTitleContainingIgnoreCase(title)).thenReturn(books);

            List<Book> result = bookService.findByTitle(title);
            assertSame(books, result);

            verify(bookRepository).findByTitleContainingIgnoreCase(title);
        }

        @DisplayName("Should return an empty list of books when no titles match")
        @Test
        void shouldReturnEmptyListWhenNoBooksMatchTitle() {
            String title = "abczdsx";
            List<Book> books = List.of();

            when(bookRepository.findByTitleContainingIgnoreCase(title)).thenReturn(books);

            List<Book> result = bookService.findByTitle(title);
            assertTrue(result.isEmpty());

            verify(bookRepository).findByTitleContainingIgnoreCase(title);
        }

    }

    @Nested
    class FindAllBooksTests {

        @DisplayName("Should return a list containing all books")
        @Test
        void shouldReturnAllBooks() {

            List<Book> books = List.of(
                    new Book(),
                    new Book()
            );

            when(bookRepository.findAll()).thenReturn(books);

            List<Book> result = bookService.findAll();
            assertSame(books, result);

            verify(bookRepository).findAll();
        }

        @DisplayName("Should return an empty list of books")
        @Test
        void shouldReturnEmptyList() {

            List<Book> books = List.of();

            when(bookRepository.findAll()).thenReturn(books);

            List<Book> result = bookService.findAll();
            assertTrue(result.isEmpty());

            verify(bookRepository).findAll();
        }

    }

    @Nested
    class CreateBookTests {

        @DisplayName("Should create a book when fields are valid")
        @Test
        void shouldCreateBook() {

            BookCreateDTO request = new BookCreateDTO();
            request.setAuthor("J.K. Rowling");
            request.setTitle("Harry Potter and the Goblet of Fire");
            request.setPublishedYear(2004);
            request.setTotalPages(389);

            Book book = BookMapper.toEntity(request);

            when(bookRepository.save(any(Book.class))).thenReturn(book);
            Book result = bookService.create(request);

            assertSame(book, result);

            verify(bookRepository).save(any(Book.class));
        }

        @DisplayName("Should throw DataIntegrityViolationException when trying to create a duplicate book")
        @Test
        void shouldThrowDataIntegrityViolationExceptionWhenCreatingDuplicateBook() {

            BookCreateDTO request = new BookCreateDTO();
            request.setAuthor("J.K. Rowling");
            request.setTitle("Harry Potter and the Goblet of Fire");
            request.setPublishedYear(2004);
            request.setTotalPages(389);

            when(bookRepository.save(any(Book.class)))
                    .thenThrow(new DataIntegrityViolationException("Duplicate book"));

            assertThrows(DataIntegrityViolationException.class, () -> {
                bookService.create(request);
            });

            verify(bookRepository).save(any(Book.class));
        }
    }

    @Nested
    class UpdateBookTests {
        @DisplayName("Should update a book when id exists")
        @Test
        void shouldUpdateBookWhenIdExists(){
            Long id = 1L;

            Book existingBook = new Book();
            existingBook.setTitle("Old Title");
            existingBook.setAuthor("Old Author");

            BookUpdateDTO request = new BookUpdateDTO();
            request.setTitle("New Title");
            request.setAuthor("New Author");

            when(bookRepository.findById(id))
                    .thenReturn(Optional.of(existingBook));

            Book result = bookService.update(id, request);

            assertSame(existingBook, result);
            assertEquals("New Title", result.getTitle());
            assertEquals("New Author", result.getAuthor());

            verify(bookRepository).findById(id);
        }

        @DisplayName("Should throw BookNotFoundException when updating non existing book")
        @Test
        void shouldThrowBookNotFoundExceptionWhenUpdatingNonExistingBook() {

            Long id = 1L;

            BookUpdateDTO request = new BookUpdateDTO();
            request.setTitle("New Title");

            when(bookRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(BookNotFoundException.class, () -> {
                bookService.update(id, request);
            });

            verify(bookRepository).findById(id);
        }
    }

    @Nested
    class DeleteBookTests{
        @DisplayName("Should delete book when id exists")
        @Test
        void shouldDeleteBookWhenIdExists() {

            Long id = 1L;

            Book book = new Book();

            when(bookRepository.findById(id))
                    .thenReturn(Optional.of(book));

            bookService.delete(id);

            verify(bookRepository).findById(id);
            verify(bookRepository).delete(book);
        }

        @DisplayName("Should throw BookNotFoundException when deleting non existing book")
        @Test
        void shouldThrowBookNotFoundExceptionWhenDeletingNonExistingBook() {

            Long id = 1L;

            when(bookRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(BookNotFoundException.class, () -> {
                bookService.delete(id);
            });

            verify(bookRepository).findById(id);
        }
    }
}