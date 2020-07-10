package org.origin.crawler.article.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author roochy
 * @package org.origin.crawler.article.entity
 * @since 2019/10/17
 */
@Data
@ToString
public class Info implements Serializable {
    private long id;
    private String title;
    private String content;
    private String tags;
    private String author;
    private String source;
    private String publishTime;
}
