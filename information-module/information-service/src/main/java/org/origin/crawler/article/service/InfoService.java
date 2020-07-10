package org.origin.crawler.article.service;

import com.github.pagehelper.Page;
import org.origin.crawler.article.entity.Info;
import org.origin.crawler.article.entity.InfoType;

import java.util.List;

/**
 * @author roochy
 * @package org.origin.crawler.article.service
 * @since 2019/10/17
 */
public interface InfoService {
    List<InfoType> listInfoType();

    Page<Info> pageListInfo(Info info, int pageIndex, int pageSize);

    List<Info> listInfo(Info info);

    Info selectInfo(Info info);

    int addInfo(Info info);
}
