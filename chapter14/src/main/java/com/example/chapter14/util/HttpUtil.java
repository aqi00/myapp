package com.example.chapter14.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil {
    private final static String TAG = "HttpUtil";
    private final static int CONNECT_TIMEOUT = 15000;
    private final static int READ_TIMEOUT = 15000;

    // 兼容https开头的调用地址
    private static void compatibleSSL(String callUrl) throws Exception {
        if (callUrl.toLowerCase().startsWith("https")) {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
    }

    // 对指定接口地址发起GET调用
    public static String get(String callUrl, Map<String, String> headers) {
        String resp = ""; // 应答内容
        try {
            Log.d(TAG, "请求地址："+callUrl);
            compatibleSSL(callUrl); // 兼容https开头的调用地址
            URL url = new URL(callUrl); // 根据网址字符串构建URL对象
            // 打开URL对象的网络连接，并返回HttpURLConnection连接对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // 设置请求方式
            setConnHeader(conn, headers);// 设置HTTP连接的头部信息
            conn.connect(); // 开始连接
            // 打印HTTP调用的应答内容长度、内容类型、压缩方式
            Log.d(TAG,  String.format("应答内容长度=%d, 内容类型=%s, 压缩方式=%s",
                    conn.getContentLength(), conn.getContentType(), conn.getContentEncoding()) );
            // 对输入流中的数据解压和字符编码，得到原始的应答字符串
            resp = getUnzipString(conn);
            // 打印HTTP调用的应答状态码和应答报文
            Log.d(TAG,  String.format("应答状态码=%d, 应答报文=%s", conn.getResponseCode(), resp) );
            conn.disconnect(); // 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    // 从指定url获取图片
    public static Bitmap getImage(String callUrl, Map<String, String> headers) {
        Bitmap bitmap = null; // 位图对象
        try {
            Log.d(TAG, "请求地址："+callUrl);
            compatibleSSL(callUrl); // 兼容https开头的调用地址
            URL url = new URL(callUrl); // 根据网址字符串构建URL对象
            // 打开URL对象的网络连接，并返回HttpURLConnection连接对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // 设置请求方式
            setConnHeader(conn, headers);// 设置HTTP连接的头部信息
            conn.connect(); // 开始连接
            // 打印图片获取的应答内容长度、内容类型、压缩方式
            Log.d(TAG,  String.format("应答内容长度=%d, 内容类型=%s, 压缩方式=%s",
                    conn.getContentLength(), conn.getContentType(), conn.getContentEncoding()) );
            // 对输入流中的数据解码，得到位图对象
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            // 打印图片获取的应答状态码和位图大小
            Log.d(TAG,  String.format("应答状态码=%d, 位图大小=%s", conn.getResponseCode(), bitmap.getByteCount()) );
            conn.disconnect(); // 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 对指定接口地址发起POST调用
    public static String post(String callUrl, String req, Map<String, String> headers) {
        String resp = ""; // 应答内容
        try {
            Log.d(TAG, "请求地址："+callUrl+", 请求报文="+req);
            compatibleSSL(callUrl); // 兼容https开头的调用地址
            URL url = new URL(callUrl); // 根据网址字符串构建URL对象
            // 打开URL对象的网络连接，并返回HttpURLConnection连接对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // 设置请求方式
            setConnHeader(conn, headers);// 设置HTTP连接的头部信息
            conn.setRequestProperty("Content-Type", "application/json"); // 请求报文为json格式
            conn.setDoOutput(true); // 准备让连接执行输出操作。默认为false，POST方式需要设置为true
            //conn.setDoInput(true); // 准备让连接执行输入操作。默认为true
            conn.connect(); // 开始连接
            OutputStream os = conn.getOutputStream(); // 从连接对象中获取输出流
            os.write(req.getBytes()); // 往输出流写入请求报文
            // 打印HTTP调用的应答内容长度、内容类型、压缩方式
            Log.d(TAG,  String.format("应答内容长度=%s, 内容类型=%s, 压缩方式=%s",
                    conn.getHeaderField("Content-Length"), conn.getHeaderField("Content-Type"),
                    conn.getHeaderField("Content-Encoding")) );
            // 对输入流中的数据解压和字符编码，得到原始的应答字符串
            resp = getUnzipString(conn);
            // 打印HTTP调用的应答状态码和应答报文
            Log.d(TAG,  String.format("应答状态码=%d, 应答报文=%s", conn.getResponseCode(), resp) );
            conn.disconnect(); // 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    // 把文件上传给指定的URL
    public static String upload(String uploadUrl, String uploadFile, Map<String, String> headers) {
        String resp = ""; // 应答内容
        // 从本地文件路径获取文件名
        String fileName = uploadFile.substring(uploadFile.lastIndexOf("/"));
        String end = "\r\n"; // 结束字符串
        String hyphens = "--"; // 连接字符串
        String boundary = "WUm4580jbtwfJhNp7zi1djFEO3wNNm"; // 边界字符串
        try (FileInputStream fis = new FileInputStream(uploadFile)) {
            Log.d(TAG, "上传地址："+uploadUrl+", 上传文件="+uploadFile);
            compatibleSSL(uploadUrl); // 兼容https开头的调用地址
            URL url = new URL(uploadUrl); // 根据网址字符串构建URL对象
            // 打开URL对象的网络连接，并返回HttpURLConnection连接对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // 设置请求方式
            setConnHeader(conn, headers);// 设置HTTP连接的头部信息
            conn.setDoOutput(true); // 准备让连接执行输出操作。默认为false，POST方式需要设置为true
            //conn.setDoInput(true); // 准备让连接执行输入操作。默认为true
            conn.setRequestProperty("Connection", "Keep-Alive"); // 连接过程要保持活跃
            // 请求报文要求分段传输，并且各段之间以边界字符串隔开
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // 根据连接对象的输出流构建数据输出流
            DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
            // 以下写入请求报文的头部
            ds.writeBytes(hyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file\";filename=\"" + fileName + "\"" + end);
            ds.writeBytes(end);
            // 以下写入请求报文的主体
            byte[] buffer = new byte[1024];
            int length;
            // 先将文件数据写入到缓冲区，再将缓冲数据写入输出流
            while ((length = fis.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            // 以下写入请求报文的尾部
            ds.writeBytes(hyphens + boundary + hyphens + end);
            ds.close(); // 关闭数据输出流
            // 打印HTTP调用的应答内容长度、内容类型、压缩方式
            Log.d(TAG,  String.format("应答内容长度=%s, 内容类型=%s, 压缩方式=%s",
                    conn.getHeaderField("Content-Length"), conn.getHeaderField("Content-Type"),
                    conn.getHeaderField("Content-Encoding")) );
            // 对输入流中的数据解压和字符编码，得到原始的应答字符串
            resp = getUnzipString(conn);
            // 打印HTTP上传的应答状态码和应答报文
            Log.d(TAG,  String.format("应答状态码=%d, 应答报文=%s", conn.getResponseCode(), resp) );
            conn.disconnect(); // 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    // 设置HTTP连接的头部信息
    private static void setConnHeader(HttpURLConnection conn, Map<String, String> headers) {
        conn.setConnectTimeout(CONNECT_TIMEOUT); // 设置连接的超时时间，单位毫秒
        conn.setReadTimeout(READ_TIMEOUT); // 设置读取应答数据的超时时间，单位毫秒
        conn.setRequestProperty("Accept", "*/*"); // 设置数据格式
        conn.setRequestProperty("Accept-Language", "zh-CN"); // 设置文本语言
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate"); // 设置编码格式
        // 设置用户代理，包括操作系统版本、浏览器版本等等
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");
        if (headers != null) {
            for (Map.Entry<String, String> item : headers.entrySet()) {
                conn.setRequestProperty(item.getKey(), item.getValue());
            }
        }
    }

    // 把输入流中的数据按照指定字符编码转换为字符串。处理大量数据需要使用本方法
    private static String isToString(InputStream is, String charset) {
        String result = "";
        // 创建一个字节数组的输出流对象
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int i = -1;
            while ((i = is.read()) != -1) { // 循环读取输入流中的字节数据
                baos.write(i); // 把字节数据写入字节数组输出流
            }
            byte[] data = baos.toByteArray(); // 把字节数组输出流转换为字节数组
            result = new String(data, charset); // 将字节数组按照指定的字符编码生成字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // 返回转换后的字符串
    }

    // 从HTTP连接中获取已解压且重新编码后的应答报文
    private static String getUnzipString(HttpURLConnection conn) throws IOException {
        String contentType = conn.getContentType(); // 获取应答报文的内容类型（包括字符编码）
        String charset = "UTF-8"; // 默认的字符编码为UTF-8
        if (contentType != null) {
            if (contentType.toLowerCase().contains("charset=gbk")) { // 应答报文采用gbk编码
                charset = "GBK"; // 字符编码改为GBK
            } else if (contentType.toLowerCase().contains("charset=gb2312")) { // 采用gb2312编码
                charset = "GB2312"; // 字符编码改为GB2312
            }
        }
        String contentEncoding = conn.getContentEncoding(); // 获取应答报文的压缩方式
        InputStream is = conn.getInputStream(); // 获取HTTP连接的输入流对象
        String result = "";
        if (contentEncoding != null && contentEncoding.contains("gzip")) { // 应答报文使用gzip压缩
            // 根据输入流对象构建压缩输入流
            try (GZIPInputStream gis = new GZIPInputStream(is)) {
                // 把压缩输入流中的数据按照指定字符编码转换为字符串
                result = isToString(gis, charset);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 把输入流中的数据按照指定字符编码转换为字符串
            result = isToString(is, charset);
        }
        return result; // 返回处理后的应答报文
    }

}
