package org.origin.crawler.article.dao;

import org.origin.crawler.article.entity.Info;
import org.origin.crawler.article.entity.InfoType;

import java.util.List;

/**
 * @author roochy
 * @package org.origin.crawler.article.dao
 * @since 2019/10/17
 */
public interface InfoTypeDAO {
    List<InfoType> list();
}
