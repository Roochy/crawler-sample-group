package org.origin.crawler.article.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author roochy
 * @package org.origin.crawler.article.util
 * @since 2019/10/17
 */
public class TagUtils {
    public enum AipTag {
        BACK_END("be", 0), FRONT_END("fe", 1), DATABASE("db", 2), JAVA("java", 3), CPLUS("c", 4), NETTY("netty", 5);

        private String desc;
        private int val;

        AipTag(String desc, int val) {
            this.desc = desc;
            this.val = val;
        }

        public int val() {return val;}
        public static int val(String desc) {
            for (AipTag tag : AipTag.values()) {
                if (desc.equals(tag.desc)) {
                    return tag.val();
                }
            }
            return BACK_END.val();
        }
        public String desc() {return desc;}
    }

    public enum Indicator {
        URL(1), ELEMENT(2);
        private int val;

        Indicator(int val) {
            this.val = val;
        }

        public int val() {return val;}
    }

    public static final String formulateTags(String tag) {
        if (tag == null || tag.trim().equals("")) {
            return tag;
        }
        String[] tagStrList = tag.split(",");
        List<Integer> tagList = new ArrayList<>();
        for (String tagStr : tagStrList) {
            tagList.add(Integer.parseInt(tagStr));
        }
        for (int i = 0; i < tagList.size(); i++) {
            for (int j = i+1; j < tagList.size(); j++) {
                if (tagList.get(i) > tagList.get(j)) {
                    int tmp = tagList.get(i);
                    tagList.set(i, tagList.get(j));
                    tagList.set(j, tmp);
                }
            }
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tagList.size(); i++) {
            buffer.append(tagList.get(i));
            if(i != tagList.size()-1) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    public static final String mapTagVal2Desc(String tag) {
        if (tag == null || tag.trim().equals("")) {
            return tag;
        }
        String[] tagStrList = tag.split(",");
        List<Integer> tagList = new ArrayList<>();
        for (String tagStr : tagStrList) {
            tagList.add(Integer.parseInt(tagStr));
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tagList.size(); i++) {
            for (AipTag t : AipTag.values()) {
                if (tagList.get(i) == t.val()) {
                    buffer.append(t.desc());
                }
            }
            if(i != tagList.size()-1) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    /**
     * 标签描述映射为值
     * @param tag
     * @return
     */
    public static final String mapDesc2Val(String tag) {
        if (tag == null || tag.trim().equals("")) {
            return tag;
        }
        String[] tagStrList = tag.split(",");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tagStrList.length; i++) {
            for (AipTag t : AipTag.values()) {
                if (tagStrList[i].toLowerCase().equals(t.desc.toLowerCase())) {
                    buffer.append(t.val());
                }
            }
            if(i != tagStrList.length-1) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}
