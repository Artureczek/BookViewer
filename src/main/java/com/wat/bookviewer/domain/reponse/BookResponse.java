package com.wat.bookviewer.domain.reponse;
import java.util.List;

/**
 * Created by akielbiewski on 21.11.2016.
 */
public class BookResponse<T> {

    private Integer total;
    private List<T> result;

    public BookResponse(List<T> result, Integer total) {
        this.total = total;
        this.result = result;
    }

    public Integer getTotal() {
        return total;
    }

    public List<T> getResult() {
        return result;
    }
}
