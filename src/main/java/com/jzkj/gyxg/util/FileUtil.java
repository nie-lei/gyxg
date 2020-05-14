package com.jzkj.gyxg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

public final class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger("runtimeLogger");
	
	private FileUtil() {}
	
	/**
	 * 取得 java.io.File 对象
	 * 
	 * @param pathname
	 * @return
	 */
	public static File getFileObject(String pathname) {
		if (StringUtil.isEmpty(pathname)) {
			return null;
		}
		
		return new File(pathname);
	}
	
	/**
	 * 取得文件的扩展名
	 * 
	 * @param pathname
	 * @return
	 */
	public static String getFileExtension(String pathname) {
		if (StringUtil.isEmpty(pathname) || !pathname.contains(".")) {
			return "";
		}
		
		return pathname.substring(pathname.lastIndexOf(".") + 1).toLowerCase();
	}
	
	/**
	 * 检查是否为文件
	 * 
	 * @param pathname
	 * @return
	 */
	public static boolean isFile(String pathname) {
		return isFile(getFileObject(pathname));
	}
	
	/**
	 * 检查是否为文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isFile(File file) {
		if (file == null) {
			return false;
		}
		
		return file.isFile();
	}
	
	/**
	 * 检查是否为目录
	 * 
	 * @param pathname
	 * @return
	 */
	public static boolean isDirectory(String pathname) {
		return isDirectory(getFileObject(pathname));
	}
	
	/**
	 * 检查是否为目录
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isDirectory(File file) {
		if (file == null) {
			return false;
		}
		
		return file.isDirectory();
	}
	
	/**
	 * 读取 properties 文件
	 * 
	 * @param pathname
	 * @return
	 */
	public static Properties loadPropertiesFromClassPathResource(String pathname) {
		Properties properties = new Properties();
		
		try {
			properties.load(new BufferedReader(new InputStreamReader(FileUtil.class.getClassLoader().getResourceAsStream(pathname), "utf-8")));
			if (properties == null || properties.isEmpty()) {
				return null;
			}
			else {
				return properties;
			}
		} catch (Exception e) {
			ExceptionUtil.logStackTrace(logger, e);
			return null;
		}
	}
}