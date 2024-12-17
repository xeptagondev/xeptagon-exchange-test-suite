//package Hooks;
//
//
//import CommonClasses.ScreenshotUtil;
//import io.cucumber.java.After;
//import io.cucumber.java.Scenario;
//import org.openqa.selenium.WebDriver;
//
//public class Hooks {
//    WebDriver driver;
//
//    // Constructor to initialize WebDriver
//    public Hooks(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    @After
//    public void takeScreenshotAfterScenario(Scenario scenario) {
//        // Take screenshot after each scenario (pass or fail)
//        if (scenario.isFailed()) {
//            ScreenshotUtil.takeScreenshot(driver, "failed_scenario_" + scenario.getName());
//        } else {
//            ScreenshotUtil.takeScreenshot(driver, "passed_scenario_" + scenario.getName());
//        }
//    }
//}
//
