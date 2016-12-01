package com.wat.bookviewer.domain.reponse;

import com.wat.bookviewer.domain.Book;

import java.time.LocalDate;

/**
 * Created by akielbiewski on 23.11.2016.
 */
public class SingleBookRespone {

    private Book result;
    private String status;
    private Integer purchaseId;
    private LocalDate expDate;


    public SingleBookRespone(Book result, String status, Integer purchaseId, LocalDate expDate) {
        this.result = result;
        this.status = status;
        this.purchaseId = purchaseId;
        this.expDate = expDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public Book getResult() {
        return result;
    }

    public void setResult(Book result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }
}
