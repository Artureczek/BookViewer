package com.wat.bookviewer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Rate.
 */

@Document(collection = "rate")
public class Rate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Field("userId")
    private String userId;

    @Field("bookId")
    private Long bookId;

    @Field("rate")
    private Long rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public Rate userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Rate bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getRate() {
        return rate;
    }

    public Rate rate(Long rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rate rate = (Rate) o;
        if(rate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Rate{" +
            "id=" + id +
            ", userId='" + userId + "'" +
            ", bookId='" + bookId + "'" +
            ", rate='" + rate + "'" +
            '}';
    }
}
