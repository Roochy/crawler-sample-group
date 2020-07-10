package org.origin.crawler.pipeline;

import org.origin.crawler.article.entity.Info;
import org.origin.crawler.article.service.InfoService;
import org.origin.crawler.article.util.TagUtils;
import org.origin.crawler.config.AppLogger;
import org.origin.util.UrlUtil;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author roochy
 * @package org.origin.crawler.pipeline
 * @since 2019/10/17
 */
public class InfoPipeline implements Pipeline {
    private InfoService infoService;

    public InfoPipeline(InfoService infoService) {
        this.infoService = infoService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        Info info = resultItems.get("info");
        if (info == null) {
            return;
        }
        checkInfo(info);
        infoService.addInfo(info);
        AppLogger.app.info("【保存成功】“{}” 保存成功.", info.getTitle());
        AppLogger.app.info("===========================");
    }

    private void checkInfo(Info info) {
        if (StringUtils.isEmpty(info.getAuthor())) {
            info.setAuthor(UrlUtil.extractDomainWithoutProtocol(info.getSource()));
        }
        if (StringUtils.isEmpty(info.getPublishTime())) {
            info.setPublishTime(DateFormat.getDateTimeInstance().format(new Date()));
        }
        info.setTags(TagUtils.formulateTags(info.getTags()));
    }
}
