//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jzkj.gyxg.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class HttpsUtils {
    public HttpsUtils() {
    }

    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();

        try {
            TrustManager[] e = new TrustManager[]{new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init((KeyManager[])null, e, new SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod(requestMethod);
            if("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            OutputStream inputStream;
            if(outputStr != null) {
                inputStream = httpUrlConn.getOutputStream();
                inputStream.write(outputStr.getBytes("UTF-8"));
                inputStream.close();
            }

            InputStream inputStream1 = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream1, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;

            while((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream1.close();
            httpUrlConn.disconnect();
            inputStream = null;
            bufferedReader = null;
            inputStreamReader = null;
            httpUrlConn = null;
            return buffer.toString();
        } catch (ConnectException var13) {
            var13.printStackTrace();
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        return null;
    }

    public static String httpsRequestByWxCert(String keyfilename, String mchId, String requestUrl, String requestMethod, String outputStr) {
        File keyFile = new File(keyfilename);
        CloseableHttpClient httpclient = null;
        StringBuffer buffer = new StringBuffer();

        try {
            httpclient = HttpClientUtil.getClient(keyFile, mchId);
        } catch (UnrecoverableKeyException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | KeyManagementException var58) {
            return null;
        }

        try {
            CloseableHttpResponse e1 = null;
            StringEntity bufferedReader;
            if("POST".equals(requestMethod)) {
                HttpPost e = new HttpPost(requestUrl);
                if(StringUtil.isNull(outputStr)) {
                    bufferedReader = new StringEntity(outputStr, "UTF-8");
                    e.setEntity(bufferedReader);
                }

                System.out.println("executing request" + e.getRequestLine());
                e1 = httpclient.execute(e);
            } else if("GET".equals(requestMethod)) {
                HttpGet el = new HttpGet(requestUrl);
                System.out.println("executing request" + el.getRequestLine());
                e1 = httpclient.execute(el);
            }

            if(e1 != null) {
                try {
                    HttpEntity e2 = e1.getEntity();
                    System.out.println("----------------------------------------");
                    System.out.println(e1.getStatusLine());
                    if(e2 != null) {
                        System.out.println("Response content length: " + e2.getContentLength());
                        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(e2.getContent()));

                        try {
                            String text;
                            try {
                                while((text = bufferedReader1.readLine()) != null) {
                                    buffer.append(text);
                                }
                            } catch (IOException var59) {
                                return null;
                            }
                        } finally {
                            bufferedReader1.close();
                            bufferedReader = null;
                        }
                    }

                    EntityUtils.consume(e2);
                    return buffer.toString();
                } catch (IOException var61) {
                    return null;
                } finally {
                    try {
                        e1.close();
                        e1 = null;
                    } catch (IOException var57) {
                    }

                }
            }

            return null;
        } catch (IOException var63) {
        } finally {
            try {
                httpclient.close();
                httpclient = null;
            } catch (IOException var56) {
            }

        }
        return null;
    }
}
