package utils.Http;

import controller.MainController;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;

public class HttpTools{

    //默认百度爬虫UA头
    public static final String UA = "Mozilla/5.0 (Macintosh; Intel Mac 0s X 10.15: ry:109.0)Gecko/20100101 Firefox/115.0";


    //GET方法
    public static Response get(String url, HashMap<String,String> headers, String encoding){
        Response response = new Response(0, null, null, null);
        try {
            HttpURLConnection conn = getCoon(url);
            conn.setRequestMethod("GET");
            Iterator var4 = headers.keySet().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                conn.setRequestProperty(key, headers.get(key));
            }

            response = getResponse(conn, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    //旧POST方法
    public static Response post(String url, String postString, HashMap<String, String> headers, String encoding) {
        Response response = new Response(0, null, null, null);
        try {
            HttpURLConnection conn = getCoon(url);
            conn.setRequestMethod("POST");
            Iterator var5 = headers.keySet().iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                conn.setRequestProperty(key, headers.get(key));
            }

            conn.setDoOutput(true);
            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(postString.replace("\n", "\r\n").getBytes(encoding));
                outputStream.flush();
            }
            response = getResponse(conn, encoding);
        } catch (Exception e) {
            System.out.println("连接异常");
        }
        return response;
    }

    //新POST方法
//    public static Response post(String url, String postString, HashMap<String, String> headers, String encoding) {
//        Response response = new Response(0, null, null, null);
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//            conn.setRequestMethod("POST");
//
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                conn.setRequestProperty(entry.getKey(), entry.getValue());
//            }
//
//            conn.setDoOutput(true);
//            try (OutputStream outputStream = conn.getOutputStream()) {
//                outputStream.write(postString.getBytes(encoding));
//                outputStream.flush();
//            }
//
//            response = getResponse(conn, encoding);
//        } catch (Exception e) {
//            System.out.println("连接异常");
//        }
//        return response;
//    }



    //PUT方法
    public static Response put(String url, String postString, HashMap<String,String> headers , String encoding){
        Response response = new Response(0, null, null, null);
        try {
            HttpURLConnection coon = getCoon(url);
            coon.setRequestMethod("PUT");
            Iterator var5 = headers.keySet().iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                coon.setRequestProperty(key, headers.get(key));
            }
            OutputStream outputStream = coon.getOutputStream();
            outputStream.write(postString.getBytes());
            outputStream.flush();
            outputStream.close();
            response = getResponse(coon, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //文件上传
    public static Response upload(String url, HashMap<String,String> headers,byte[] start_data,byte[] end_data,String filePath,String encoding){
        Response response = new Response(0, null, null, null);
        try {
            HttpURLConnection coon = getCoon(url);
            coon.setRequestMethod("POST");
            coon.setDoOutput(true);
            coon.setDoInput(true);
            coon.setUseCaches(false);
            Iterator var5 = headers.keySet().iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                coon.setRequestProperty(key, headers.get(key));
            }

            OutputStream out = new DataOutputStream(coon.getOutputStream());
            File file = new File(filePath);
            if(start_data != null) {
                out.write(start_data);
            }
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            while ((bytes = in.read(bufferOut)) != -1){
                out.write(bufferOut,0,bytes);
            }
            out.write("\r\n".getBytes());
            in.close();
            if(end_data != null) {
                out.write(end_data);
            }
            out.flush();
            out.close();

            response = getResponse(coon, encoding);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    //获取响应对象
//   public static Response getResponse(HttpURLConnection conn, String encoding){
//       Response response = new Response(0, null, null, null);
//
//       try {
//           conn.connect();
//           conn.setConnectTimeout(3000);
//           response.setCode(conn.getResponseCode());
//           response.setHead(conn.getHeaderFields().toString());
//           response.setText(streamToString(conn.getInputStream(), encoding));
//       } catch (Exception e) {
//           e.printStackTrace();
//       }
//
//       return response;
//   }
   //获取相应对象旧代码
   public static Response getResponse(HttpURLConnection conn, String encoding){
       Response response = new Response(0, null, null, null);

       try {
           conn.connect();
           conn.setConnectTimeout(3000);
           response.setCode(conn.getResponseCode());
           response.setHead(conn.getHeaderFields().toString());
           response.setText(streamToString(conn.getInputStream(), encoding));
       } catch (Exception e) {
//           e.printStackTrace();
       }

       return response;
   }

    //创建连接 其余请求方式皆由该方法创建连接
    public static HttpURLConnection getCoon(String url) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException{
        SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
        TrustManager[] tm = new TrustManager[]{new Cert()};
        sslcontext.init(null, tm, new SecureRandom());
        HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
            public boolean verify(String s, SSLSession sslsession) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        URL url_object = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)url_object.openConnection();
        //代理
        Proxy proxy = (Proxy) MainController.settingInfo.get("proxy");
        if(proxy != null) {
            conn = (HttpURLConnection)url_object.openConnection(proxy);
        }

        conn.setRequestProperty("User-Agent", UA);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
        conn.setRequestProperty("Connection","close");

        conn.setConnectTimeout(100000);
        conn.setReadTimeout(100000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(false);
        return conn;

    }

    private static String streamToString(InputStream inputStream, String encoding) {
        String resultString = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        byte[] data = new byte[1024];

        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
            if(encoding.equals("")) {
                encoding = "UTF-8";
            }
            resultString = byteArrayOutputStream.toString(encoding);
        } catch (IOException var6) {
            resultString = var6.getMessage();
            var6.printStackTrace();
        }

        return resultString;
    }
}