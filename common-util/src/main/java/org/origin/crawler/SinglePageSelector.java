package org.origin.crawler;

import lombok.Data;

/**
 * @author roochy
 * @package org.origin.crawler
 * @since 2019/10/16
 */
@Data
public class SinglePageSelector {
    private String title;
    private String content;
    private String tag;
    private String author;
    private String publishTime;
}
