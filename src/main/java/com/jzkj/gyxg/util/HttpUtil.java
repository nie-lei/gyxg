package com.jzkj.gyxg.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HttpUtil {
	private final static Logger errorLogger = LoggerFactory.getLogger("errorLogger");
	private static CloseableHttpClient client;
	private static RequestConfig defaultConfig;
	private static final String PATHNAME = "/home/Music/image";

	static {
		defaultConfig = RequestConfig.custom()
				.setSocketTimeout(6000)
				.setConnectTimeout(6000)
				.setConnectionRequestTimeout(6000)
				.build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(3000);
		cm.setDefaultMaxPerRoute(1000);
		HttpHost httpHost = new HttpHost("61.145.229.26",8086);
		cm.setMaxPerRoute(new HttpRoute(httpHost),1000);

		client = HttpClients.custom()
				.setConnectionManager(cm)
				.setDefaultRequestConfig(defaultConfig)
				.build();
	}
	
	private HttpUtil() {}
	
	/**
	 * 模拟 GET 请求
	 * 
	 * @param requestUrl
	 * @return
	 */
	public static String doGet(String requestUrl) {
		// 创建httpget.
		HttpGet httpget = new HttpGet(requestUrl);
		try {
			// 执行get请求.
			CloseableHttpResponse response = client.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			Header[] headers = response.getAllHeaders();
			for (Header header: headers) {
				System.out.println(header);
			}
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		}  catch (Exception e) {
			errorLogger.error(e.getMessage());
			return "";
		}

		return "";
	}
	
	/**
	 * 模拟 POST 请求
	 * 
	 * @param requestUrl
	 * @param params
	 * @return
	 */
	public static String doPost(String requestUrl, Map<String, String> params) {
		return doPost(requestUrl,params,"UTF_8");
	}

	/**
	 * 模拟 POST 请求
	 *
	 * @param requestUrl
	 * @param params
	 * @return
	 */
	public static String doPost(String requestUrl, Map<String, String> params, String charset) {
		// 创建httppost
		HttpPost httppost = new HttpPost(requestUrl);
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<>();
		if(params!=null && !params.isEmpty()){
			for (String paramName : params.keySet()) {
				formparams.add(new BasicNameValuePair(paramName, params.get(paramName)));
			}
		}

		try {
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formparams,charset);
			httppost.setEntity(postEntity);
			httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
			CloseableHttpResponse response = client.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String res = EntityUtils.toString(entity, "UTF-8");
					return res;
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			errorLogger.error(e.getMessage());
			return "";
		}

		return "";
	}

	/**
	 * 模拟 POST 请求
	 *
	 * @param requestUrl
	 * @param params
	 * @return
	 */
	public static String singlePost(String requestUrl, Map<String, String> params, String charset) {
		// 创建httppost
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(requestUrl);
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<>();
		if(params!=null && !params.isEmpty()){
			for (String paramName : params.keySet()) {
				formparams.add(new BasicNameValuePair(paramName, params.get(paramName)));
			}
		}

		try {
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(formparams,charset);
			httppost.setEntity(postEntity);
			CloseableHttpResponse response = httpClient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, "UTF-8");
				}
			} finally {
				response.close();
				httpClient.close();
			}
		} catch (Exception e) {
			errorLogger.error(e.getMessage());
			return "";
		}
		return "";
	}

	/**
	 * 模拟 POST 请求
	 *
	 * @param requestUrl
	 * @param charset
	 * @return
	 */
	public static String doPost(String requestUrl, String charset) {
		// 创建httppost
		HttpPost httppost = new HttpPost(requestUrl);
		try {
			CloseableHttpResponse response = client.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String res = EntityUtils.toString(entity,charset);
					return res;
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			errorLogger.error(e.getMessage());
			return "";
		} finally {
			httppost.releaseConnection();
		}
		return "";
	}


	/**
	 * 模拟 POST 请求
	 *
	 * @param requestUrl
	 * @param jsonContent
	 * @return
	 */
	public static String doPostJson(String requestUrl, String jsonContent, String charset) {
		// 创建httppost
		HttpPost httppost = new HttpPost(requestUrl);
		httppost.setHeader("Content-Type","application/data");
		try {
			StringEntity postEntity = new StringEntity(jsonContent,charset);
			httppost.setEntity(postEntity);
			CloseableHttpResponse response = client.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, charset);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			errorLogger.error(e.getMessage());
			return "";
		}
		return "";
	}

	public static String doPostJsonStr(String postUrl, String postData) {
		try {
			//发送POST请求
			URL url = new URL(postUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Length", "" + postData.length());
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(postData);
			out.flush();
			out.close();

			//获取响应状态
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("connect failed!");
				return "";
			}
			//获取响应内容体
			String line, result = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			in.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return "";
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param
	 * ，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param url
	 *            发送请求的 URL
	 * @return 所代表远程资源的响应结果
	 */
	public static Map sendPost_JSON(String url, JSONObject obj) {
		//int  result = 0 ;
		Map rstJson = null;
		String result = "";
		BufferedReader in = null;
		OutputStream out = null;
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			conn = (HttpURLConnection) realUrl.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");

			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = conn.getOutputStream();
			// 发送请求参数
			out.write((obj.toString()).getBytes());
			// flush输出流的缓冲
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			rstJson = (Map) JSON.parse(result);
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return rstJson;
	}

	/**
	 * 获取客户端ip
	 *
	 * @return
	 */
	public static String getClientIp() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = req.getHeader("X-Real-IP");

		if (!StringUtil.isEmpty(ip) && StringUtil.isNumeric(ip.replace(".", ""))) {
			return ip;
		}

		ip = req.getHeader("X-Forwarded-For");

		if (!StringUtil.isEmpty(ip) && StringUtil.isNumeric(ip.replace(".", ""))) {
			return ip;
		}

		ip = req.getRemoteAddr();

		if (!StringUtil.isEmpty(ip) && StringUtil.isNumeric(ip.replace(".", ""))) {
			return ip;
		}

		return "127.0.0.1";
	}

	public static String send(String url, Map<String,String> map,String encoding, String cookie) throws ParseException, IOException {
		String body = "";

		//创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		//装填参数 
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(map!=null){
			for (Map.Entry<String, String> entry : map.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		//设置参数到请求对象中
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

		//设置header信息
		//指定报文头【Content-type】、【User-Agent】
		httpPost.setHeader("Accept", "application/json, text/plain, */*");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Cookie", cookie);

		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");

		//执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		//获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			//按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		//释放链接
		response.close();
		return body;
	}

	public static Map<String, Object> doget(String url, Map<String,String> map) throws ParseException, IOException {
		Map<String, Object> map1 = new HashMap<String, Object>();
	    String body = "";

		//创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		//创建post方式请求对象
		HttpGet httpGet = new HttpGet(url);
		//装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if(map!=null){
			for (Map.Entry<String, String> entry : map.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		//设置header信息
		//指定报文头【Content-type】、【User-Agent】
		httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		httpGet.setHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		httpGet.setHeader("Connection", "keep-alive");
		//执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpGet);
		//获取结果实体
        Header[] headers = response.getAllHeaders();
        for (Header header: headers) {
            map1.put(header.getName(), header.getValue());
        }
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			//按指定编码转换结果实体为String类型
//			body = EntityUtils.toString(entity, encoding);
			InputStream inputStream=entity.getContent();
			// 获取自己数组
			byte[] getData = readInputStream(inputStream);
			// 文件保存位置
			File saveDir = new File(PATHNAME);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}
			String fileName =  StringUtil.getRandomString(10) + ".png";
			File file = new File(saveDir + File.separator + fileName);
//        File file = new File(saveDir + File.separator + DateUtil.format(new Date(), "yyyyMMdd") + StringUtil.getRandomString(6, 2) + fileName.substring(fileName.lastIndexOf(".")));
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(getData);
			body = PATHNAME + "/" + fileName;
		}
		EntityUtils.consume(entity);
		//释放链接
		response.close();

		map1.put("body", body);
		return map1;
	}

	/**
	 * 从输入流中获取数据
	 *
	 * @param inStream 输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}


}