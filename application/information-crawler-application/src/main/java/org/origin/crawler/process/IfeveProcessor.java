package org.origin.crawler.process;

import org.origin.cache.RedisCache;
import org.origin.crawler.SinglePageSelector;
import org.origin.crawler.TagMap;
import org.origin.crawler.article.entity.Info;
import org.origin.crawler.config.AppLogger;
import org.origin.crawler.config.GlobalConfig;
import org.origin.util.TimeUtil;
import org.origin.util.UrlUtil;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.*;

/**
 * @author roochy
 * @package org.origin.crawler.process
 * @since 2017/12/17
 */
public class IfeveProcessor implements PageProcessor {
    private RedisCache redis;
    private String domain;
    private Site site;

    public IfeveProcessor(RedisCache redis) {
        this.redis = redis;
        this.domain = GlobalConfig.getCurrentDomain();
        this.site = Site.me()
                .setDomain(domain)
                .setRetryTimes(3)
                .setSleepTime(200)
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
    }

    /**
     * 从 URL 提取类型
     * @param url
     * @return
     */
    private String extractTag(String url) {
        if (UrlUtil.trimParamters(url).matches(GlobalConfig.crawler.getListPageReg())) {
            // 替换通用地址段
            url = UrlUtil.trimProtocol(url);
            url = url.replace("/category", "");
            // 截取主域名后地址段
            int speratorIndex = url.indexOf("/");
            if (speratorIndex <= 0) {
                return null;
            }
            url = url.substring(speratorIndex + 1);
            // 去除无关地址段
            speratorIndex = url.indexOf("/");
            if (speratorIndex > 0) {
                return url.substring(0, speratorIndex);
            }
            return url;
        } else if (UrlUtil.trimParamters(url).matches(GlobalConfig.crawler.getSinglePageReg())) {
            Map<String, String> params = UrlUtil.getParameters(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getKey().equals("tag")) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }


    /**
     * 判断是否为目标 URL
     * @param url
     * @return
     */
    private boolean isTargetUrl(String url) {
        String tag = extractTag(url);
        if (StringUtils.isEmpty(tag)) {
            return false;
        }
        List<TagMap> tagMaps = GlobalConfig.crawler.getTagMapList();
        for (TagMap tagMap : tagMaps) {
            if (tagMap.getOrigin().equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将源网站的标签映射为本系统标签
     * @param url
     * @return
     */
    private String mapTag(String url) {
        String tag = extractTag(url);
        if (StringUtils.isEmpty(tag)) {
            return "0";
        }
        List<TagMap> tagMaps = GlobalConfig.crawler.getTagMapList();
        for (TagMap tagMap : tagMaps) {
            if (tagMap.getOrigin().equals(tag)) {
                return tagMap.getAip();
            }
        }
        return "0";
    }

    /**
     * 给种子添加参数以便识别
     * @param url
     * @param tag
     * @return
     */
    private String appendTagParamOnSeed(String url, String tag) {
        url = UrlUtil.trimParamters(url);
        return url.concat("?tag=" + tag);
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        // 无类型参数的URL
        String url = page.getUrl().get();
        AppLogger.app.info("===========================");
        AppLogger.app.info("【开始处理】{} ", url);
        String compareUrl = UrlUtil.trimParamters(url);
        try {
            // 处理列表
            if (compareUrl.equals(domain)) {
                List<String> listSeeds = html.links().regex(GlobalConfig.crawler.getListPageReg()).all();
                Set<String> filteredSeeds = new HashSet<>(listSeeds);
                int count = 0;
                for (String seed : filteredSeeds) {
                    if (!isTargetUrl(seed)) {
                        continue;
                    }
                    addToQueue(seed);
                    count++;
                }
                AppLogger.app.info("【链接增量】本次新增任务 {} 个", count);
            } else if (compareUrl.matches(GlobalConfig.crawler.getListPageReg())) {
                List<String> listSeeds = html.css("#left_col .post .title").links().regex(GlobalConfig.crawler.getSinglePageReg()).all();
                listSeeds.addAll(html.css("#left_col .page-numbers").links().regex(GlobalConfig.crawler.getListPageReg()).all());
                Set<String> filteredSeeds = new HashSet<>(listSeeds);
                String seed;
                Iterator<String> iterator = filteredSeeds.iterator();
                String tag = extractTag(url);
                int count = 0;
                while (iterator.hasNext()) {
                    seed = iterator.next();
                    if (isDuplicate(seed)) {
                        continue;
                    }
                    // 列表页爬取的链接处理后添加到任务队列
                    if (seed.matches(GlobalConfig.crawler.getSinglePageReg())) {
                        seed = appendTagParamOnSeed(seed, tag);
                    }
                    if (!isTargetUrl(seed)) {
                        continue;
                    }
                    addToQueue(seed);
                    count++;
                }
                AppLogger.app.info("【链接增量】本次新增任务 {} 个", count);
            } else if (compareUrl.matches(GlobalConfig.crawler.getSinglePageReg())) {
                Info info = new Info();
                info.setTitle(html.getDocument().select(getSelectorConfig().getTitle()).text());
                info.setContent(html.getDocument().select(getSelectorConfig().getContent()).html());
                if (!StringUtils.isEmpty(info.getTitle()) && !StringUtils.isEmpty(info.getContent())) {
                    info.setSource(url);
                    info.setAuthor(html.getDocument().select(getSelectorConfig().getAuthor()).text());
                    info.setPublishTime(html.getDocument().select(getSelectorConfig().getPublishTime()).text());
                    info.setTags(mapTag(url));
                    page.putField("info", info);
                    AppLogger.app.info("【处理结果】“{}” 爬取成功", info.getTitle());
                }
            }
            // 标记已爬取
            AppLogger.app.info("【处理完成】{}", url);
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.app.info("【处理失败】Error occurred while processing page {}, message: {}", url, e.getMessage());
        }
    }

    private SinglePageSelector getSelectorConfig() {
        return GlobalConfig.crawler.getSinglePageSelector();
    }

    public boolean isDuplicate(String url) {
        String historyUrl = UrlUtil.trimParamters(url);
        historyUrl = UrlUtil.trimProtocol(historyUrl);
        if (historyUrl.equals(UrlUtil.trimProtocol(domain))) {
            return redis.exists(GlobalConfig.getDomainKey());
        } else if (url.matches(GlobalConfig.crawler.getListPageReg()) && !url.contains("page")) {
            return redis.exists(historyUrl);
        }
        return redis.sismember(GlobalConfig.getHistoryKey(), historyUrl);
    }

    private void addToQueue(String url) {
        redis.sadd(GlobalConfig.getQueueKey(), url);
    }

    @Override
    public Site getSite() {
        return site;
    }
}
