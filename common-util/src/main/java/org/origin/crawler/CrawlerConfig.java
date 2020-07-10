package org.origin.crawler;

import lombok.Data;

import java.util.List;

/**
 * @author roochy
 * @package org.origin.crawler
 * @since 2019/10/16
 */
@Data
public class CrawlerConfig {
    private String portal;
    private String listPageReg;
    private String singlePageReg;
    private SinglePageSelector singlePageSelector;
    private List<TagMap> tagMapList;
}
