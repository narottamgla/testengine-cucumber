package com.api.hooks;

import com.api.urls.BaseUrl;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class Hooks {

    @Before(order = 1)
    public void getScenarioDetails(final Scenario scenario) {
        Storage.putScenario(scenario);
    }

    @BeforeAll
    public static void setup() throws Exception {
        log.info("Running on environment: {}", System.getProperty("env"));

        try (InputStream inputStream = Hooks.class.getClassLoader()
                .getResourceAsStream("api.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            BaseUrl.AIO.setBaseUrl(properties.getProperty("AIO_GOV"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}