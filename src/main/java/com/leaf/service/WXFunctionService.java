package com.leaf.service;

import com.leaf.entity.constant.WeiXinChartConfig;
import com.leaf.entity.wxResult.JSConfig;
import com.leaf.utils.WXJSSDKUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/******************************************************
 *Copyrights @ 2017，xiaowo  Co., Ltd.
 *
 *Author:
 *		 yecanyi
 *Finished：
 *		2017/10/13
 ********************************************************/
@Service
public class WXFunctionService {
    //这种配置方式，需要在applicationContext.xml文件中配置相应的bean
    //@Value("#{wxProperties['wechat.appId']}")
    @Value("${wechat.appId}")
    private String appId;
    //@Value("#{wxProperties['wechat.secret']}")
    @Value("${wechat.secret}")
    private String secret;

    /**
     * 微信公众号网页开发获取接口配置
     * @param request
     * @return
     */
    public JSConfig getJSConfig(HttpServletRequest request){
        String accessToken = WXJSSDKUtils.getAccessToken(appId,secret);
        String jsapiTicket = WXJSSDKUtils.getTicket(accessToken,WeiXinChartConfig.TICKET_TYPE_JSAPI);
        String url = request.getRequestURL().toString();
        JSConfig jsConfig = WXJSSDKUtils.sign(jsapiTicket,url,appId);
        return jsConfig;
    }
}
