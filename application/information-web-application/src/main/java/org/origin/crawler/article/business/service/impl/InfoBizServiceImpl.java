package org.origin.crawler.article.business.service.impl;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.origin.crawler.article.business.service.InfoBizService;
import org.origin.crawler.article.entity.Info;
import org.origin.crawler.article.service.InfoService;
import org.origin.crawler.article.util.TagUtils;
import org.origin.web.PageData;
import org.origin.web.PageResponse;
import org.origin.web.Response;
import org.origin.web.ResponseCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author roochy
 * @package org.origin.crawler.article.business.service.impl
 * @since 2019/10/18
 */
@Slf4j
@Service("infoBizService")
public class InfoBizServiceImpl implements InfoBizService {
    @Resource(name = "infoService")
    private InfoService infoService;

    @Override
    public Response<Info> selectInfo(Info param) {
        log.info("查询信息详情参数: {}", param.toString());
        Response<Info> ret;
        try {
            param.setTags(TagUtils.mapDesc2Val(param.getTags()));
            param.setTags(TagUtils.formulateTags(param.getTags()));
            Info info = infoService.selectInfo(param);
            info.setTags(TagUtils.mapTagVal2Desc(info.getTags()));
            param.setTags(TagUtils.mapTagVal2Desc(param.getTags()));
            ret = new Response<>(ResponseCode.SUCCESS, info, ResponseCode.SUCCESS_MSG);
        } catch (Exception e) {
            log.error("查询信息详情失败: {}", e);
            return new Response<>();
        }
        return ret;
    }

    @Override
    public PageResponse<Info> queryInfoList(Info info, int pageIndex, int pageSize) {
        log.info("查询信息列表参数: pageIndex:{}, pageSize:{}, info:{}", pageIndex, pageSize, info.toString());
        PageResponse<Info> ret;
        try {
            info.setTags(TagUtils.mapDesc2Val(info.getTags()));
            info.setTags(TagUtils.formulateTags(info.getTags()));
            Page<Info> page = infoService.pageListInfo(info, pageIndex, pageSize);
            for (Info i : page.getResult()) {
                i.setTags(TagUtils.mapTagVal2Desc(i.getTags()));
            }
            info.setTags(TagUtils.mapTagVal2Desc(info.getTags()));
            PageData<Info> pageData = new PageData<>(pageIndex, page.getPages(), page.getTotal(), page.getResult());
            ret = new PageResponse<>(ResponseCode.SUCCESS, pageData, ResponseCode.SUCCESS_MSG);
        } catch (Exception e) {
            log.error("查询信息详情失败: {}", e);
            return new PageResponse<>();
        }
        return ret;
    }
}
