package org.origin.crawler.config;

import org.origin.crawler.CrawlerConfig;
import org.origin.crawler.article.util.NameSpaceConstant;
import org.origin.util.ConfigLoader;
import org.origin.util.UrlUtil;

/**
 * @author roochy
 * @package org.origin.crawler.config
 * @since 2019/10/17
 */
public class GlobalConfig {
    public static final CrawlerConfig crawler = ConfigLoader.instance()
            .loadJsonConfig("/rules.json", CrawlerConfig.class);

    public static final String getCurrentDomain() {
        return UrlUtil.extractDomain(crawler.getPortal());
    }

    public static final String getPortal() {
        return crawler.getPortal();
    }

    public static String getDomainKey() {
        return getCurrentDomain() + NameSpaceConstant.PORTAL_SUFFIX;
    }

    public static String getQueueKey() {
        return getCurrentDomain() + NameSpaceConstant.TASK_QUEUE_SUFFIX;
    }

    public static String getHistoryKey() {
        return getCurrentDomain() + NameSpaceConstant.HISTORY_SUFFIX;
    }
}
