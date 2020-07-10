package org.origin.crawler.article.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author roochy
 * @package org.origin.crawler.article.entity
 * @since 2019/10/17
 */
@Data
public class InfoType implements Serializable {
    private long id;
    private String desc;
}
