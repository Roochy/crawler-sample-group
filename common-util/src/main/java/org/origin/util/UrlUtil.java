package org.origin.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author roochy
 * @package org.origin.util
 * @since 2019/10/17
 */
public class UrlUtil {
    /**
     * 返回无协议前缀的URL
     * @param url
     * @return
     */
    public static String trimProtocol(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        return url.replace("https://", "")
                .replace("http://","")
                .replace("ftp://","");
    }

    /**
     * 去除锚定符号
     * @param url
     * @return
     */
    public static String trimAnchor(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        int paramIndex = url.indexOf("#");
        if (paramIndex <= 0) {
            return url;
        }
        return url.substring(0, paramIndex);
    }

    /**
     * 获取 URL 中的参数
     * @param url
     * @return
     */
    public static Map<String, String> getParameters(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        int paramIndex = url.indexOf("?");
        if (paramIndex <= 0) {
            return null;
        }
        url = trimAnchor(url);
        String paramStr = url.substring(paramIndex + 1);
        String[] paramList = paramStr.split("&");
        String[] paramPair;
        Map<String, String> paramMap = new HashMap<>();
        for (String param : paramList) {
            paramPair = param.split("=");
            if (paramPair.length <= 1) {
                continue;
            }
            paramMap.put(paramPair[0], paramPair[1]);
        }
        return paramMap;
    }

    /**
     * 返回无参数的 URL
     * @param url
     * @return
     */
    public static String trimParamters(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        url = trimAnchor(url);
        int paramIndex = url.indexOf("?");
        if (paramIndex <= 0) {
            return url;
        }
        return url.substring(0, paramIndex);
    }

    /**
     * 获取主域名
     * @param url
     * @return
     */
    public static String extractDomain(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }
        url = trimParamters(url);
        String wUrl = trimProtocol(url);
        int separateIndex = wUrl.indexOf("/");
        if (separateIndex != -1) {
            separateIndex = (url.length() - wUrl.length()) + separateIndex;
            return url.substring(0, separateIndex);
        } else {
            return url;
        }
    }

    /**
     * 返回主域名（无参数）
     * @param url
     * @return
     */
    public static String extractDomainWithoutProtocol(String url) {
        return trimProtocol(extractDomain(url));
    }
}
