package com.wat.bookviewer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Book.
 */

@Document(collection = "Books")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Field("isbn")
    private String isbn;

    @Field("title")
    private String title;

    @Field("author")
    private String author;

    @Field("year")
    private Long year;

    @Field("publisherId")
    private Long publisherId;

    @Field("imgSmall")
    private String imgSmall;

    @Field("imgMedium")
    private String imgMedium;

    @Field("imgLarge")
    private String imgLarge;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getYear() {
        return year;
    }

    public Book year(Long year) {
        this.year = year;
        return this;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public Book publisherId(Long publisherId) {
        this.publisherId = publisherId;
        return this;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public Book imgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
        return this;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgMedium() {
        return imgMedium;
    }

    public Book imgMedium(String imgMedium) {
        this.imgMedium = imgMedium;
        return this;
    }

    public void setImgMedium(String imgMedium) {
        this.imgMedium = imgMedium;
    }

    public String getImgLarge() {
        return imgLarge;
    }

    public Book imgLarge(String imgLarge) {
        this.imgLarge = imgLarge;
        return this;
    }

    public void setImgLarge(String imgLarge) {
        this.imgLarge = imgLarge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        if(book.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", isbn='" + isbn + "'" +
            ", title='" + title + "'" +
            ", author='" + author + "'" +
            ", year='" + year + "'" +
            ", publisherId='" + publisherId + "'" +
            ", imgSmall='" + imgSmall + "'" +
            ", imgMedium='" + imgMedium + "'" +
            ", imgLarge='" + imgLarge + "'" +
            '}';
    }
}
