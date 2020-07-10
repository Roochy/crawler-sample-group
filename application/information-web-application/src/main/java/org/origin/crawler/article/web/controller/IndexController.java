package org.origin.crawler.article.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.origin.crawler.article.business.service.InfoBizService;
import org.origin.crawler.article.entity.Info;
import org.origin.crawler.article.util.TagUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author roochy
 * @package org.origin.crawler.article.web.controller
 * @since 2018/1/16
 */
@Controller
@Slf4j
public class IndexController {
    @Resource(name = "infoBizService")
    private InfoBizService infoBizService;

    @RequestMapping("/")
    public String index() {
        return "forward:WEB-INF/views/page.html";
    }

    @RequestMapping(value = {"/info", "/info/{pageIndex}"})
    @ResponseBody
    public Model page(Model model, Info info,
                        @PathVariable(value = "pageIndex", required = false) @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                        @RequestParam(value = "pageSize", defaultValue = "15") int pageSize) {
        model.addAttribute("info", info);
        model.addAttribute("response", infoBizService.queryInfoList(info, pageIndex, pageSize));
        return model;
    }

    @RequestMapping("/info/{tag}/{pageIndex}")
    @ResponseBody
    public Model tagPage(Model model, Info info,
                       @PathVariable(value = "tag") String tag,
                       @PathVariable(value = "pageIndex") int pageIndex,
                       @RequestParam(value = "pageSize", defaultValue = "15") int pageSize) {
        info.setTags(String.valueOf(TagUtils.AipTag.val(tag)));
        model.addAttribute("response", infoBizService.queryInfoList(info, pageIndex, pageSize));
        info.setTags(tag);
        model.addAttribute("info", info);
        return model;
    }

    @RequestMapping("/info/{infoId}")
    @ResponseBody
    public Model view(Model model, Info info,
                       @PathVariable(value = "infoId") long infoId) {
        info.setId(infoId);
        model.addAttribute("info", info);
        model.addAttribute("response", infoBizService.selectInfo(info));
        return model;
    }
}
