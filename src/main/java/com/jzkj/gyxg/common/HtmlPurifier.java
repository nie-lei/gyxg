package com.jzkj.gyxg.common;

import com.jzkj.gyxg.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public final class HtmlPurifier {
	public static String clean(String str) {
		if (StringUtil.isEmpty(str)) {
			return "";
		}
		return Jsoup.clean(str, Whitelist.none());
		/*return str;*/
	}
}
