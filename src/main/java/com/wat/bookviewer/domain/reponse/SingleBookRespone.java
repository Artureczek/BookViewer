package com.wat.bookviewer.domain.reponse;

import com.wat.bookviewer.domain.Book;

import java.time.LocalDate;

/**
 * Created by akielbiewski on 23.11.2016.
 */
public class SingleBookRespone {

    private Book result;

    private LocalDate finishDate;

    public SingleBookRespone(Book result, LocalDate finishDate) {
        this.result = result;
        this.finishDate = finishDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public Book getResult() {
        return result;
    }

    public void setResult(Book result) {
        this.result = result;
    }
}
