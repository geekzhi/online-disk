package com.geekzhang.demo.util;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/24 上午10:43
 * @version: V1.0
 */
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用Java原生API编写发送HTTP_POST请求的工具类
 * @see 本工具类仅用于学习了解,实际中推荐使用HttpClientUtil工具类
 * @see 地址为http://blog.csdn.net/jadyer/article/details/8087960
 * @create Mar 4, 2013 2:47:57 PM
 * @author 玄玉<http://blog.csdn/net/jadyer>
 */
public class HttpUtil {
    private HttpUtil(){}

    /**
     * 发送HTTP_POST请求
     * @param reqURL   请求地址
     * @param sendData 发送到远程主机的正文数据
     * @return HTTP响应码`远程主机响应正文,如<code>"200`SUCCESS"</code>
    若通信过程中发生异常则返回"HTTP响应码`Failed",如<code>"500`Failed"</code>
     */
    public static String sendPostRequest(String reqURL, String sendData){
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; //写
        InputStream in = null;   //读
        int responseCode = 0;    //远程主机响应的HTTP状态码
        try{
            URL sendUrl = new URL(reqURL);
            httpURLConnection = (HttpURLConnection)sendUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);        //指示应用程序要将数据写入URL连接,其值默认为false
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); //30秒连接超时
            httpURLConnection.setReadTimeout(30000);    //30秒读取超时

            out = httpURLConnection.getOutputStream();
            out.write(sendData.toString().getBytes());
            out.flush(); //清空缓冲区,发送数据

            //使用httpURLConnection.getResponseMessage()可以获取到[HTTP/1.0 200 OK]中的[OK]
            responseCode = httpURLConnection.getResponseCode();

            ////处理返回结果
            //BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            //String row = null;
            //String respData = "";
            //if((row=br.readLine()) != null){ //readLine()方法在读到换行[/n]或回车[/r]时,即认为该行已终止
            //  respData = row;              //HTTP协议POST方式的最后一行数据为正文数据
            //}
            //br.close();
            in = httpURLConnection.getInputStream();
            byte[] byteDatas = new byte[in.available()];
            in.read(byteDatas);
            return responseCode + "`" + new String(byteDatas);
        }catch(Exception e){
            System.out.println("与[" + reqURL + "]通信异常,堆栈信息为");
            e.printStackTrace();
            return responseCode + "`" + "Failed`";
        }finally{
            if(out != null){
                try{
                    out.close();
                }catch (Exception e){
                    System.out.println("关闭输出流时发生异常,堆栈信息如下");
                    e.printStackTrace();
                }
            }
            if(in != null){
                try{
                    in.close();
                }catch(Exception e){
                    System.out.println("关闭输入流时发生异常,堆栈信息如下");
                    e.printStackTrace();
                }
            }
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }
    }


    /**
     * 发送HTTP_POST请求
     * @param reqURL 请求地址
     * @param params 发送到远程主机的正文数据
     * @return HTTP响应码`远程主机响应正文,如<code>"200`SUCCESS"</code>
    若通信过程中发生异常则返回"HTTP响应码`Failed",如<code>"500`Failed"</code>
     */
    public static String sendPostRequest(String reqURL, Map<String, String> params){
        StringBuilder sendData = new StringBuilder();
        for(Map.Entry<String, String> entry : params.entrySet()){
            sendData.append(entry.getKey()).append("=").append(entry.getValue()).append("&;");
        }
        if(sendData.length() > 0){
            sendData.setLength(sendData.length() - 1); //删除最后一个&;符号
        }
        return sendPostRequest(reqURL, sendData.toString());
    }
}
