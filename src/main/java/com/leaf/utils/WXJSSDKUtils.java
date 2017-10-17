package com.leaf.utils;

import com.alibaba.fastjson.JSONObject;
import com.leaf.entity.wxResult.JSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

/******************************************************
 *Copyrights @ 2017，xiaowo  Co., Ltd.
 *
 *Author:
 * 微信JSSDK的各个接口，在调用之前需要获取安全参数如access_token等
 * 这里归纳一些通用方法
 *		 yecanyi
 *Finished：
 *		2017/08/26
 ********************************************************/
public class WXJSSDKUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WXJSSDKUtils.class);

    /**
     * 生成签名实体
     * @param jsapi_ticket JS_API临时票据
     * @param url          请求地址，参考微信文档
     * @return
     */
    public static JSConfig sign(String jsapi_ticket, String url, String appId) {
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println("string1=" + string1);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            String signature = byteToHex(crypt.digest());
            JSConfig jsConfig = new JSConfig();
            jsConfig.setNonceStr(nonce_str);
            jsConfig.setTimestamp(timestamp);
            jsConfig.setSignature(signature);
            jsConfig.setAppId(appId);
            return jsConfig;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密
     * @param hash
     * @return
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 随机数
     * @return
     */
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    /**
     * 时间戳
     * @return
     */
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取access_token
     * @param appid
     * @param secret
     * @return
     */
    public static String getAccessToken(String appid, String secret) {
        try {
            String getAccessTokenURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&"
                    + "appid=" + appid + "&secret=" + secret;
            String accessTokenXml = HttpClientUtils.doGet(getAccessTokenURL);
            JSONObject accessTokeJsonMap = JSONObject.parseObject(accessTokenXml);
            return accessTokeJsonMap.getString("access_token");
        } catch (Exception e) {
            System.out.println("获取access_token失败。。。。。。");
            return "";
        }
    }

    /**
     * 获取临时票据
     *
     * @param access_token
     * @param type         wx_card:卡卷ticket jsapi:JsApiTicket
     * @return
     */
    public static String getTicket(String access_token, String type) {
        try {
            String getTicketURL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=" + type;
            String ticketXml = HttpClientUtils.doGet(getTicketURL);
            JSONObject ticketJsonMap = JSONObject.parseObject(ticketXml);
            return ticketJsonMap.getString("ticket");
        } catch (Exception e) {
            System.out.println("获取" + type + "类型的ticket失败");
            return "";
        }
    }


    /**
     * 从微信获取临时素材，并保存到自己的服务器
     * @param request
     * @param accessToken
     * @param mediaId
     * @return
     */
    public static String saveImageToDisk(HttpServletRequest request, String accessToken, String mediaId) {
        String saveUrl = "";
        InputStream inputStream = getMedia(accessToken,mediaId);
        if (inputStream == null){
            return saveUrl;
        }
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            //获取根目录
            String root = request.getSession().getServletContext().getRealPath("/fintecher_file");
            //文件名
            String filename = UUID.randomUUID().toString() + ".jpg";
            //先创建文件夹，避免fileOutputStream报错
            File file = new File(root + File.separator + "images");
            if (!file.exists()){
                file.mkdirs();
            }
            String url = root + "/images/" + filename;
            fileOutputStream = new FileOutputStream(url);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
            saveUrl = "/fintecher_file/images/" +filename;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return saveUrl;
        }
    }

    public static InputStream getImageFromWx(HttpServletRequest request, String accessToken, String mediaId){
        InputStream inputStream = getMedia(accessToken,mediaId);
        return inputStream;
    }

    /**
     * 从微信获取临时素材
     * @param accessToken
     * @param mediaId
     * @return
     */
    private static InputStream getMedia(String accessToken,String mediaId) {
        String url = "https://api.weixin.qq.com/cgi-bin/media/get";
        String params = "access_token=" + accessToken + "&media_id=" + mediaId;
        InputStream is = null;
        try {
            String urlNameString = url + "?" + params;
            URL urlGet = new URL(urlNameString);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            // 获取文件转化为byte流
            is = http.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return is;
        }
        return is;
    }

    /**
     * 获取微信用户openId
     * @param appid
     * @param domain
     * @return
     */
    public static String getOpenId(String appid, String domain){
        try {
            String requestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri="
                    + URLEncoder.encode("http://" + domain + "/weixin/authorization/back", "utf-8")
                    + "&response_type=code&scope=snsapi_base&state=iXiaoWo#wechat_redirect";
            String openIdXml = HttpClientUtils.doGet(requestUrl);
            JSONObject openIdJsonMap = JSONObject.parseObject(openIdXml);
            return openIdJsonMap.getString("openid");
        } catch (Exception e) {
            System.out.println("获取access_token失败。。。。。。");
            return "";
        }
    }
}
