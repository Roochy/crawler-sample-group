package org.origin.crawler.shceduler;

import org.origin.cache.RedisCache;
import org.origin.crawler.config.AppLogger;
import org.origin.crawler.config.GlobalConfig;
import org.origin.util.TimeUtil;
import org.origin.util.UrlUtil;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

/**
 * @author roochy
 * @package org.origin.crawler
 * @since 2019/10/17
 */
public class RedisScheduler implements Scheduler {
    private RedisCache redis;
    private String domain;

    public RedisScheduler(RedisCache redis) {
        this.redis = redis;
        domain = GlobalConfig.getCurrentDomain();
    }

    public String getDomainKey() {
        return GlobalConfig.getDomainKey();
    }

    public String getQueueKey() {
        return GlobalConfig.getQueueKey();
    }

    public String getHistoryKey() {
        return GlobalConfig.getHistoryKey();
    }

    @Override
    public void push(Request request, Task task) {
        if (isDuplicate(request, task)) {
            return;
        }
        redis.sadd(getQueueKey(), request.getUrl());
    }

    @Override
    public synchronized Request poll(Task task) {
        Object url;
        try {
            url = redis.spop(getQueueKey());
            // 队列无任务时，自旋等待执行
            while (url == null) {
                // 队列长时间为空或首次运行时，爬取主页
                if (!redis.exists(getDomainKey())) {
                    url = domain;
                    break;
                }
                // 在10s内首页已经爬取过，等待并重新从队列中获取元素
                wait(10000);
                url = redis.spop(getQueueKey());
            }
        } catch (Exception e) {
            AppLogger.app.error("【队列异常】从队列中获取任务失败, {}", e.getMessage());
            url = domain;
        }
        archieve((String)url);
        return new Request((String) url);
    }

    public boolean isDuplicate(Request request, Task task) {
        String url = request.getUrl();
        String historyUrl = UrlUtil.trimParamters(url);
        historyUrl = UrlUtil.trimProtocol(historyUrl);
        if (historyUrl.equals(UrlUtil.trimProtocol(domain))) {
            return redis.exists(GlobalConfig.getDomainKey());
        } else if (url.matches(GlobalConfig.crawler.getListPageReg()) && !url.contains("page")) {
            return redis.exists(historyUrl);
        }
        return redis.sismember(getHistoryKey(), historyUrl);
    }

    private void archieve(String url) {
        String historyUrl = UrlUtil.trimProtocol(url);
        historyUrl = UrlUtil.trimParamters(historyUrl);
        if (UrlUtil.trimProtocol(domain).equals(historyUrl)) {
            redis.set(getDomainKey(), domain);
            redis.expire(getDomainKey(), TimeUtil.ONE_DAY_SECONDS);
        } else if (url.matches(GlobalConfig.crawler.getListPageReg()) && !url.contains("page")) {
            redis.set(historyUrl, historyUrl);
            redis.expire(historyUrl, TimeUtil.ONE_DAY_SECONDS);
        } else {
            redis.sadd(getHistoryKey(), historyUrl);
        }
    }
}
