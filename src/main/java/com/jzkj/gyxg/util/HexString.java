package com.jzkj.gyxg.util;

import java.io.ByteArrayOutputStream;

public class HexString {
	/*
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2) {
			try {
				baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
						.indexOf(bytes.charAt(i + 1))));
			} catch (Exception e) {
				return null;
			}
		}
		return new String(baos.toByteArray());
	}

	/**
	 * 字符串中指定位置新增指定字符串
	 * 
	 * @param str
	 *            原字符串
	 * @param num
	 *            新增位置
	 * @param repstr
	 *            指定字符串
	 * @return 新增后的字符串
	 */
	public static String insertstr(String str, int num, String repstr) {
		if(StringUtil.isEmpty(str))
			return "";
		if(str.length() < num)
			return str;
		StringBuffer sb = new StringBuffer(str);
		sb = sb.insert(num, repstr);
		return sb.toString();
	}

	/**
	 * 字符串中指定位置替换指定字符串
	 * @param str 元字符串
	 * @param num 删除其实位置
	 * @param removestr 删除字符串
	 * @param repalestr 替换字符串
	 * @return
	 */
	public static String removestr(String str, int num, String removestr, String repalestr) {
		if(StringUtil.isEmpty(str))
			return "";
		if(str.length() < num + removestr.length())
			return str;
		StringBuffer sb = new StringBuffer(str);
		sb = sb.replace(num, removestr.length() + num, repalestr);
		return sb.toString();
	}
}