package com.wat.bookviewer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Countries.
 */

@Document(collection = "countries")
public class Countries implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Field("name")
    private String name;

    @Field("currency")
    private String currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Countries name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public Countries currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Countries countries = (Countries) o;
        if(countries.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, countries.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Countries{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", currency='" + currency + "'" +
            '}';
    }
}
