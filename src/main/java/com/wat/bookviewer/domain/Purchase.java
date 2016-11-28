package com.wat.bookviewer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Purchase.
 */

@Document(collection = "Purchase")
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Field("date")
    private LocalDate date;

    @Field("value")
    private Double value;

    @Field("bookId")
    private Long bookId;

    @Field("userId")
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Purchase date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public Purchase value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getBookId() {
        return bookId;
    }

    public Purchase bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public Purchase userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Purchase purchase = (Purchase) o;
        if(purchase.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, purchase.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Purchase{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", value='" + value + "'" +
            ", bookId='" + bookId + "'" +
            ", userId='" + userId + "'" +
            '}';
    }
}
