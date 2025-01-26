package com.web.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/api/",
        glue = {"com.api.steps","com.api.hooks"},
        dryRun = false,
        plugin = {"html:target/report/reports.html","timeline:target/report/threadreport",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}

)
public class APIBaseTest {
}
