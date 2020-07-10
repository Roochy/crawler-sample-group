package org.origin.crawler.start;

import org.origin.cache.RedisCache;
import org.origin.crawler.article.service.InfoService;
import org.origin.crawler.config.AppLogger;
import org.origin.crawler.pipeline.InfoPipeline;
import org.origin.crawler.process.IfeveProcessor;
import org.origin.crawler.shceduler.RedisScheduler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;

/**
 * @author roochy
 * @package org.origin.crawler.start
 * @since 2017/12/23
 */
@SpringBootApplication
@ImportResource("classpath:spring/spring.xml")
public class Application implements ApplicationRunner {
    @Resource(name = "redisCache")
    private RedisCache redis;
    @Resource(name = "infoService")
    private InfoService infoService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        AppLogger.app.info("Spring context has been initiated.");
        AppLogger.app.info("Start crawler...");
        Spider.create(new IfeveProcessor(redis))
                .setScheduler(new RedisScheduler(redis))
                .addPipeline(new InfoPipeline(infoService))
                .run();
    }
}
