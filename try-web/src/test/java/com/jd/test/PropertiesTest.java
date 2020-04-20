package com.jd.test;

import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesTest {
    @Test
    public void propTest() throws IOException {
        Properties pps = new Properties();
        pps.load(new FileInputStream("/Users/sunhuichuan/IdeaProjects/try/try-web/src/main/resource/properties/common.properties"));
        Enumeration enum1 = pps.propertyNames();//得到配置文件的名字
        while(enum1.hasMoreElements()) {
            String strKey = (String) enum1.nextElement();
            String strValue = pps.getProperty(strKey);
            System.out.println(strKey + "=" + strValue);
        }
    }
}
