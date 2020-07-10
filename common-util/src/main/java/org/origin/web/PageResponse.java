package org.origin.web;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author roochy
 * @package org.origin.crawler.article.business.entity
 * @since 2018/1/16
 */
@Data
@ToString
public class PageResponse<T> implements Serializable {
    private int code = ResponseCode.SYSTEM_ERROR;
    private String message = ResponseCode.SYSTEM_ERROR_MSG;
    private PageData<T> page;

    public PageResponse() {}
    public PageResponse(int code, PageData page, String message) {
        this.code = code;
        this.page = page;
        this.message = message;
    }
}
