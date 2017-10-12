package com.leaf;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/******************************************************
 *Copyrights @ 2017，xiaowo  Co., Ltd.
 *
 *Author:
 *		 yecanyi
 *Finished：
 *		2017/10/12
 ********************************************************/
public class PropertiesTest {

    @Value("#{wxProperties.wechat.appId}")
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Test
    public  void aa() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/spring/applicationContext.xml");
        ctx.getBean("wxProperties");
        PropertiesTest test = new PropertiesTest();
        System.out.println(test.getAppId());
    }
}
