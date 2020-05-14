package com.jzkj.gyxg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CollectionUtil {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger("errorLogger");
	
	private CollectionUtil() {}
	
	/**
	 * 检查是否为 null 或为空
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}
	
	/**
	 * 转换成 Integer[]
	 * 
	 * @param list
	 * @return
	 */
	public static Integer[] toIntegerArray(List<Integer> list) {
		if (isEmpty(list)) {
			return new Integer[0];
		}
		
		return list.toArray(new Integer[0]);
	}
	
	/**
	 * 转换成 int[]
	 * 
	 * @param list
	 * @return
	 */
	public static int[] toIntArray(List<Integer> list) {
		return ArrayUtil.convert(toIntegerArray(list));
	}
	
	/**
	 * 转换成  String[]
	 * 
	 * @param list
	 * @return
	 */
	public static String[] toStringArray(List<String> list) {
		if (isEmpty(list)) {
			return new String[0];
		}
		
		return list.toArray(new String[0]);
	}
	
	/**
	 * 去重
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List unique(List<?> list) {
		if (isEmpty(list)) {
			return list;
		}
		
		List ret = new ArrayList();
		
		for (int i = 0; i < list.size(); i++) {
			if (ret.contains(list.get(i))) {
				continue;
			}
			
			ret.add(list.get(i));
		}
		
		return ret;
	}
	
	/**
	 * join with seperator as a string
	 * 
	 * @param seperator
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String join(String seperator, List<?> list) {
		if (isEmpty(list)) {
			return "";
		}
		
		if (list.get(0).getClass().equals(Integer.class)) {
			return ArrayUtil.join(seperator, toIntArray((List<Integer>) list));
		}
		else if (list.get(0).getClass().equals(String.class)) {
			return ArrayUtil.join(seperator, toStringArray((List<String>) list));
		}
		else {
			return "";
		}
	}
	
	/**
	 * 取差集
	 * 
	 * @param lst1
	 * @param lst2
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List diff(List<?> lst1, List<?> lst2) {
		List ret = new ArrayList();
		
		for (int i = 0; i < lst1.size(); i++) {
			if (lst2.contains(lst1.get(i))) {
				continue;
			}
			
			ret.add(lst1.get(i));
		}
		
		return ret;
	}
}