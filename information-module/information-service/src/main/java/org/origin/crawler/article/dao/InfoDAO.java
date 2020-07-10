package org.origin.crawler.article.dao;

import org.origin.crawler.article.entity.Info;

import java.util.List;

/**
 * @author roochy
 * @package org.origin.crawler.article.dao
 * @since 2019/10/17
 */
public interface InfoDAO {
    List<Info> list(Info info);

    Info select(Info info);

    int count(Info info);

    int insert(Info info);

    int delete(Info info);
}
