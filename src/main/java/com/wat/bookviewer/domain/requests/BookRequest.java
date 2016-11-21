package com.wat.bookviewer.domain.requests;

/**
 * Created by akielbiewski on 21.11.2016.
 */
public class BookRequest {
    private BookFilter filter;
    private String param;
    private String order;

    public BookFilter getFilter() {
        return filter;
    }

    public void setFilter(BookFilter filter) {
        this.filter = filter;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
