package com.jzkj.gyxg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class ArrayUtil {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger("errorLogger");
	
	private ArrayUtil() {}
	
	/**
	 * 检查数组是否为 null 或者空数组
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(int[] array) {
		return (array == null || array.length == 0);
	}
	
	/**
	 * 检查数组是否为 null 或者空数组
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Integer[] array) {
		return (array == null || array.length == 0);
	}
	
	
	/**
	 * 检查数组是否为 null 或者空数组
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(String[] array) {
		return (array == null || array.length == 0);
	}
	
	/**
	 * convert to Integer[]
	 * 
	 * @param array
	 * @return
	 */
	public static Integer[] convert(int[] array) {
		if (isEmpty(array)) {
			return new Integer[0];
		}
		
		Integer[] ret = new Integer[array.length];
		
		for (int i = 0; i < array.length; i++) {
			ret[i] = Integer.valueOf(array[i]);
		}
		
		return ret;
	}
	
	/**
	 * convert to int[]
	 * 
	 * @param array
	 * @return
	 */
	public static int[] convert(Integer[] array) {
		if (isEmpty(array)) {
			return new int[0];
		}
		
		int[] ret = new int[array.length];
		
		for (int i = 0; i < array.length; i++) {
			ret[i] = (int) array[i];
		}
		
		return ret;
	}
	
	
	/**
	 * 检查变量是否在数组中
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static boolean inArray(int needle, int[] haystack) {
		if (isEmpty(haystack)) {
			return false;
		}
		
		for (int item : haystack) {
			if (item == needle) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 检查变量是否在数组中
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static boolean inArray(Integer needle, int[] haystack) {
		return inArray((int) needle, haystack);
	}
	
	/**
	 * 检查变量是否在数组中
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static boolean inArray(int needle, Integer[] haystack) {
		return inArray(needle, convert(haystack));
	}
	
	/**
	 * 检查变量是否在数组中
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static boolean inArray(Integer needle, Integer[] haystack) {
		return inArray((int) needle, convert(haystack));
	}
	
	/**
	 * 检查变量是否在数组中
	 * 
	 * @param needle
	 * @param haystack
	 * @return
	 */
	public static boolean inArray(String needle, String[] haystack) {
		if (isEmpty(haystack)) {
			return false;
		}
		
		for (String item : haystack) {
			if (needle.equals(item)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 去重
	 * 
	 * @param array
	 * @return
	 */
	public static Integer[] unique(Integer[] array) {
		if (isEmpty(array)) {
			return new Integer[0];
		}
		
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i = 0; i < array.length; i++) {
			if (list.contains(array[i])) {
				continue;
			}
			
			list.add(array[i]);
		}
		
		return list.toArray(new Integer[0]);
	}
	
	/**
	 * 去重
	 * 
	 * @param array
	 * @return
	 */
	public static int[] unique(int[] array) {
		return convert(unique(convert(array)));
	}
	
	/**
	 * 去重
	 * 
	 * @param array
	 * @return
	 */
	public static String[] unique(String[] array) {
		if (isEmpty(array)) {
			return new String[0];
		}
		
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < array.length; i++) {
			if (list.contains(array[i])) {
				continue;
			}
			
			list.add(array[i]);
		}
		
		return list.toArray(new String[0]);
	}
	
	/**
	 * 转换成 List<Integer>
	 * 
	 * @param array
	 * @return
	 */
	public static List<Integer> toList(Integer[] array) {
		if (isEmpty(array)) {
			return new ArrayList<Integer>();
		}
		
		List<Integer> list = new ArrayList<Integer>();
		
		for (Integer item : array) {
			list.add(item);
		}
		
		return list;
	}
	
	/**
	 * 转换成 List<Integer>
	 * 
	 * @param array
	 * @return
	 */
	public static List<Integer> toList(int[] array) {
		return toList(convert(array));
	}
	
	/**
	 * 转换成 List<String>
	 * 
	 * @param array
	 * @return
	 */
	public static List<String> toList(String[] array) {
		if (isEmpty(array)) {
			return new ArrayList<String>();
		}
		
		List<String> list = new ArrayList<String>();
		
		for (String item : array) {
			list.add(item);
		}
		
		return list;
	}
	
	/**
	 * join with seperator as a string
	 * 
	 * @param seperator
	 * @param array
	 * @return
	 */
	public static String join(String seperator, Integer[] array) {
		if (isEmpty(array)) {
			return "";
		}
		
		if (seperator == null) {
			seperator = "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(seperator);
			}
			
			sb.append(array[i].toString());
		}
		
		return sb.toString();
	}
	
	/**
	 * join with seperator as a string
	 * 
	 * @param seperator
	 * @param array
	 * @return
	 */
	public static String join(String seperator, int[] array) {
		return join(seperator, convert(array));
	}
	
	/**
	 * join with seperator as a string
	 * 
	 * @param seperator
	 * @param array
	 * @return
	 */
	public static String join(String seperator, String[] array) {
		if (isEmpty(array)) {
			return "";
		}
		
		if (seperator == null) {
			seperator = "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(seperator);
			}
			
			sb.append(array[i].toString());
		}
		
		return sb.toString();
	}
}