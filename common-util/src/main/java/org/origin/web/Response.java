package org.origin.web;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author roochy
 * @package org.origin.crawler.article.business.entity
 * @since 2018/1/16
 */
@Data
@ToString
public class Response<T> implements Serializable {
    private int code = ResponseCode.SYSTEM_ERROR;
    private String message = ResponseCode.SYSTEM_ERROR_MSG;
    private T data;

    public Response() {

    }

    public Response(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
