package com.leaf.utils;

import net.sf.ehcache.util.PropertyUtil;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/******************************************************
 *Copyrights @ 2017，xiaowo  Co., Ltd.
 *
 *Author:
 *		 yecanyi
 *Finished：
 *		2017/10/12
 ********************************************************/
public class PropertiesUtil {

    private Properties properties;

    public PropertiesUtil(String fileName) {
        readProperties(fileName);
    }

    /**
     * 读取配置文件
     * @param propertiesFileName
     * @return
     */
    private void readProperties(String propertiesFileName){
        try {
            properties = new Properties();
            /*第一种，通过类加载器进行获取properties文件流*/
            InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(propertiesFileName);
            /*第二种，通过类进行获取properties文件流*/
            //InputStream inputStream = PropertyUtil.class.getResourceAsStream("/propertiesFileName");
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key读取对应的value
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * 得到所有的配置信息
     *
     * @return
     */
    public Map<?, ?> getAll() {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<?> enu = properties.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = properties.getProperty(key);
            map.put(key, value);
        }
        return map;
    }
}
