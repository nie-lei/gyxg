package com.jzkj.gyxg.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class Pinyin {
    /**
     * 得到中文首字母
     *
     * @param str
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        if(StringUtil.isNull(str))
            return "#";
        String words[] = str.split("");
        String result = "";
        for (String word: words) {
            if(StringUtil.isChinese(word + ""))
                result += (PinyinHelper.toHanyuPinyinStringArray(word.charAt(0))[0].charAt(0) + "").toUpperCase();
            else if(StringUtil.isLatter(word + ""))
                result += (word + "").toUpperCase();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getPinYinHeadChar("撒旦法你重庆"));
    }
}