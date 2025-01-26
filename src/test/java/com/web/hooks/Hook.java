package com.web.hooks;

import com.web.drivers.DriverManager;
import com.web.executiondata.AppUrl;
import com.web.executiondata.ExecutionConfig;
import io.cucumber.java.*;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class Hook {

    @BeforeAll(order = 1)
    public static void setup(){
        String activeProfile = System.getProperty("env","qa");
        log.info("Active profile: " + activeProfile);
        Properties properties = new Properties();
        log.info("Loading properties from property file");

        log.info("Resource URI:"+ Hook.class.getClassLoader().getResource("web.properties"));

        try (InputStream resourceStream = Hook.class.getClassLoader().getResourceAsStream("web.properties")) {
            if (resourceStream == null) {
                throw new RuntimeException("Resource not found: " + "web.properties");
            }
            properties.load(resourceStream);
            log.info("Properties loaded: " + properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //load properties
        AppUrl.APP_URL.setUrl(properties.getProperty("app_url"));
        log.info("Setting AppData done");
    }

    @Before(order = 2)
    public static void setupDriver(){
        log.info("Setting up driver");
        DriverManager.setDriver(ExecutionConfig.IS_REMOTE, ExecutionConfig.EXECUTION_BROWSER);
        log.info("Driver setup done");
    }

    @After(order = 2)
    public static void tearDown(){
        DriverManager.quitDriver();
        log.info("Quit Driver done");
    }

    @AfterStep
    public void addScreenshot(Scenario scenario){
        final byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot,"image/png","Screenshot:");

    }
}
