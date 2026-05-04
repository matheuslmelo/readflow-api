package com.readflow.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "books", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "author"})})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required.")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    private String title;

    @NotBlank(message = "Author is required.")
    @Size(min = 2, max = 100, message = "Author must be between 2 and 100 characters")
    private String author;

    @NotNull(message = "Published year is required.")
    @Min(value = 1900, message = "Published year must be at least 1900.")
    @Max(value = 2100, message = "Published year must be 2100 or before.")
    private Integer publishedYear;

    @NotNull(message = "Total pages are required.")
    @Min(value = 1, message = "Minimum of total pages allowed is 1.")
    @Max(value = 1000, message = "Maximum of total pages allowed is 1000.")
    private Integer totalPages;

    public Book() {
    }

    public Book(String title, String author, Integer publishedYear, Integer totalPages) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.totalPages = totalPages;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
