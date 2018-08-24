package com.dce.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@RunWith(JUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath:config/applicationContext.xml" })
public class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {
    static {
<<<<<<< HEAD
        System.setProperty("catalina.home", "E:/apache-tomcat-8.0.39/");
=======
        System.setProperty("catalina.home", "D:/apache-tomcat-8.0.53/");
>>>>>>> cd10215ca9db558f3b148e9487e2d3d921607e84
    }
    
    
}