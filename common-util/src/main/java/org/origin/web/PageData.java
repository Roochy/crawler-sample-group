package org.origin.web;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author roochy
 * @package org.origin.web
 * @since 2018/1/16
 */
@Data
@ToString
public class PageData<T> implements Serializable {
    private int pageIndex;
    private int total;
    private long count;
    private List<T> data;

    public PageData() {
    }

    public PageData(int pageIndex, int total, long count, List<T> data) {
        this.pageIndex = pageIndex;
        this.total = total;
        this.count = count;
        this.data = data;
    }
}
