package com.wat.bookviewer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A LastIndex.
 */

@Document(collection = "lastIndex")
public class LastIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("table")
    private String table;

    @Field("value")
    private Integer value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public LastIndex table(String table) {
        this.table = table;
        return this;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getValue() {
        return value;
    }

    public LastIndex value(Integer value) {
        this.value = value;
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LastIndex lastIndex = (LastIndex) o;
        if(lastIndex.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lastIndex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LastIndex{" +
            "id=" + id +
            ", table='" + table + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
