package com.jzkj.gyxg.common;

import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseHolder {
	private static HttpServletResponse response;
	
	public static void setResponse(HttpServletResponse resp) {
		response = resp;
	}
	
	public static HttpServletResponse getResponse() {
		return response;
	}
}