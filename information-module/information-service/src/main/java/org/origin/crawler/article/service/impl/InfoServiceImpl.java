package org.origin.crawler.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.origin.cache.RedisCache;
import org.origin.crawler.article.dao.InfoDAO;
import org.origin.crawler.article.dao.InfoTypeDAO;
import org.origin.crawler.article.entity.Info;
import org.origin.crawler.article.entity.InfoType;
import org.origin.crawler.article.service.InfoService;
import org.origin.crawler.article.util.NameSpaceConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author roochy
 * @package org.origin.crawler.article.service.impl
 * @since 2019/10/17
 */
@Service("infoService")
public class InfoServiceImpl implements InfoService {
    private static final Logger log = LoggerFactory.getLogger(InfoServiceImpl.class);
    @Resource(name = "infoDAO")
    private InfoDAO infoDAO;
    @Resource(name = "infoTypeDAO")
    private InfoTypeDAO infoTypeDAO;
    @Resource(name = "redisCache")
    private RedisCache redis;

    @Override
    public List<InfoType> listInfoType() {
        return infoTypeDAO.list();
    }

    @Override
    public Page<Info> pageListInfo(Info info, int pageIndex, int pageSize) {
        return PageHelper.startPage(pageIndex, pageSize).doSelectPage(() -> {infoDAO.list(info);});
    }

    @Override
    public List<Info> listInfo(Info info) {
        return infoDAO.list(info);
    }

    @Override
    public Info selectInfo(Info info) {
        return infoDAO.select(info);
    }

    @Override
    public int addInfo(Info info) {
        info.setId(redis.incr(NameSpaceConstant.INFO_ID_SEQUENCE));
        return infoDAO.insert(info);
    }
}
