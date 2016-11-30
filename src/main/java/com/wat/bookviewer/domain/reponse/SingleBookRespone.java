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


    public SingleBookRespone(Book result, String status, Integer purchaseId) {
        this.result = result;
        this.status = status;
        this.purchaseId = purchaseId;
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
