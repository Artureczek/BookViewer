package com.wat.bookviewer.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A JhiUser.
 */

@Document(collection = "jhiuser")
public class JhiUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("login")
    private String login;

    @Field("password")
    private String password;

    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

    @Field("activated")
    private String activated;

    @Field("langKey")
    private String langKey;

    @Field("createdBy")
    private String createdBy;

    @Field("createdDate")
    private LocalDate createdDay;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public JhiUser login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public JhiUser password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public JhiUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public JhiUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getActivated() {
        return activated;
    }

    public JhiUser activated(String activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public JhiUser langKey(String langKey) {
        this.langKey = langKey;
        return this;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public JhiUser createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDay() {
        return createdDay;
    }

    public JhiUser createdDay(LocalDate createdDay) {
        this.createdDay = createdDay;
        return this;
    }

    public void setCreatedDay(LocalDate createdDay) {
        this.createdDay = createdDay;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JhiUser jhiUser = (JhiUser) o;
        if(jhiUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jhiUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JhiUser{" +
            "id=" + id +
            ", login='" + login + "'" +
            ", password='" + password + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", activated='" + activated + "'" +
            ", langKey='" + langKey + "'" +
            ", createdBy='" + createdBy + "'" +
            ", createdDay='" + createdDay + "'" +
            '}';
    }
}
