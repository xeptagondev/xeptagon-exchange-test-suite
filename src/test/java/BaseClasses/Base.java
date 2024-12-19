package BaseClasses;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Base {
    protected static ExtentReports extent;
    protected static WebDriver driver;
    private static String reportPath;
    private static Map<String, ExtentTest> featureTests = new HashMap<>();

    @BeforeSuite
    public void generateReport() {
        reportPath = System.getProperty("user.dir") + "/Reports/ExecutionReport_"
                + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss").format(LocalDateTime.now());
        new File(reportPath).mkdir();
        new File(reportPath + "/screenshots").mkdir();
        new File(reportPath + "/files").mkdir();

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath + "/Report.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    public static void logReport(String featureName, Status status, String message, boolean takeScreenshot) {
        // Retrieve or create the ExtentTest for the feature
        ExtentTest test = featureTests.computeIfAbsent(featureName, k -> extent.createTest(featureName));

        if (takeScreenshot) {
            String screenshotPath = takeScreenshot();
            if (screenshotPath != null) {
                // Construct relative path for the screenshot
                String relativeScreenshotPath = "screenshots/" + Paths.get(screenshotPath).getFileName().toString();
                // Embed the screenshot inline in the Details column
                String screenshotHtml = "<a href='" + relativeScreenshotPath + "' target='_blank'>" +
                        "<img src='" + relativeScreenshotPath + "' style='width:150px;height:100px;'></a>";
                test.log(status, message + " " + screenshotHtml);
            } else {
                test.log(status, message);
            }
        } else {
            test.log(status, message);
        }
    }

    private static String takeScreenshot() {
        if (driver != null) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = reportPath + "/screenshots/" + System.currentTimeMillis() + ".png";
            try {
                Files.copy(screenshot.toPath(), Paths.get(screenshotPath));
                return screenshotPath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public class ScenarioContext {
        private static final Map<String, Object> context = new HashMap<>();

        public static void put(String key, Object value) {
            context.put(key, value);
        }

        public static Object get(String key) {
            return context.get(key);
        }
    }

    public class DriverManager {
        private static WebDriver driver;

        // Private constructor to prevent instantiation
        private DriverManager() {
        }

        public static WebDriver getDriver() {
            if (driver == null) {
                // Initialize the ChromeDriver
                driver = new ChromeDriver();

                // Set the browser to full-screen
                driver.manage().window().maximize(); // Or use fullscreen()
            }
            return driver;
        }
    }

    public class ConfigReader {
        private static Properties properties;

        static {
            try {
                FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
                properties = new Properties();
                properties.load(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load config.properties file!");
            }
        }

        public static String get(String key) {
            return properties.getProperty(key);
        }
    }

    @AfterSuite
    public void closeReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void CloseBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}