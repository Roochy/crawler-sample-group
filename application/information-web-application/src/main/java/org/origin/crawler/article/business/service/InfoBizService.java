package org.origin.crawler.article.business.service;

import org.origin.crawler.article.entity.Info;
import org.origin.web.PageResponse;
import org.origin.web.Response;

/**
 * @author roochy
 * @package org.origin.crawler.article.business.service
 * @since 2019/10/18
 */
public interface InfoBizService {
    /**
     * 查看信息详情
     * @param article
     * @return
     */
    Response<Info> selectInfo(Info article);

    /**
     * 分页查询信息
     * @param info
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageResponse<Info> queryInfoList(Info info, int pageIndex, int pageSize);
}
