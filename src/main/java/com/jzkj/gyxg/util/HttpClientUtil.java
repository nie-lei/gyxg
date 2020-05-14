//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jzkj.gyxg.util;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

public class HttpClientUtil {
    public static final String SunX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";

    public HttpClientUtil() {
    }

    public static HttpURLConnection getHttpURLConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        return httpURLConnection;
    }

    public static HttpsURLConnection getHttpsURLConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
        return httpsURLConnection;
    }

    public static String getURL(String strUrl) {
        if(strUrl != null) {
            int indexOf = strUrl.indexOf("?");
            return -1 != indexOf?strUrl.substring(0, indexOf):strUrl;
        } else {
            return strUrl;
        }
    }

    public static String getQueryString(String strUrl) {
        if(strUrl != null) {
            int indexOf = strUrl.indexOf("?");
            return -1 != indexOf?strUrl.substring(indexOf + 1, strUrl.length()):"";
        } else {
            return strUrl;
        }
    }

    public static Map queryString2Map(String queryString) {
        if(queryString != null && !"".equals(queryString)) {
            HashMap m = new HashMap();
            String[] strArray = queryString.split("&");

            for(int index = 0; index < strArray.length; ++index) {
                String pair = strArray[index];
                putMapByPair(pair, m);
            }

            return m;
        } else {
            return null;
        }
    }

    public static void putMapByPair(String pair, Map m) {
        if(pair != null && !"".equals(pair)) {
            int indexOf = pair.indexOf("=");
            if(-1 != indexOf) {
                String k = pair.substring(0, indexOf);
                String v = pair.substring(indexOf + 1, pair.length());
                if(k != null && !"".equals(k)) {
                    m.put(k, v);
                }
            } else {
                m.put(pair, "");
            }

        }
    }

    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;

        while((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    public static void doOutput(OutputStream out, byte[] data, int len) throws IOException {
        int dataLen = data.length;

        for(int off = 0; off < dataLen; dataLen -= len) {
            if(len >= dataLen) {
                out.write(data, off, dataLen);
            } else {
                out.write(data, off, len);
            }

            out.flush();
            off += len;
        }

    }

    public static SSLContext getSSLContext(FileInputStream trustFileInputStream, String trustPasswd, FileInputStream keyFileInputStream, String keyPasswd) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore trustKeyStore = KeyStore.getInstance("JKS");
        trustKeyStore.load(trustFileInputStream, str2CharArray(trustPasswd));
        tmf.init(trustKeyStore);
        char[] kp = str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);
        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);
        return ctx;
    }

    public static CloseableHttpClient getClient(File keyFile, String mchid) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(keyFile);

        try {
            keyStore.load(instream, mchid.toCharArray());
        } catch (NoSuchAlgorithmException var14) {
            var14.printStackTrace();
            throw var14;
        } catch (CertificateException var15) {
            var15.printStackTrace();
            throw var15;
        } catch (IOException var16) {
            var16.printStackTrace();
            throw var16;
        } finally {
            try {
                instream.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        SSLContext context = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(context, new String[]{"TLSv1"}, (String[])null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        return httpclient;
    }

    public static Certificate getCertificate(File cafile) throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(cafile);
        Certificate cert = cf.generateCertificate(in);
        in.close();
        return cert;
    }

    public static char[] str2CharArray(String str) {
        return str == null?null:str.toCharArray();
    }

    public static void storeCACert(Certificate cert, String alias, String password, OutputStream out) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load((InputStream)null, (char[])null);
        ks.setCertificateEntry(alias, cert);
        ks.store(out, str2CharArray(password));
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }
}
