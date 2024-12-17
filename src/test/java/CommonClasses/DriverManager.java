package CommonClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {
    private static WebDriver driver;

    // Private constructor to prevent instantiation
    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driver == null) {
            // Initialize the ChromeDriver
            driver = new ChromeDriver();

            // Set the browser to full-screen
            driver.manage().window().maximize(); // Or use fullscreen()
        }
        return driver;
    }

    // Optional: Method to quit the driver
//    public static void quitDriver() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//        }
//    }
}


//package CommonClasses;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//public class DriverManager {
//    private static WebDriver driver;
//
//    // Private constructor to prevent instantiation
//    private DriverManager() {}
//
//    public static WebDriver getDriver() {
//        if (driver == null) {
//            // Initialize ChromeOptions
//            ChromeOptions options = new ChromeOptions();
//
//            // Initialize the WebDriver with ChromeOptions
//            driver = new ChromeDriver(options);
//
//            // Set the browser window size to match iPhone X dimensions
//            driver.manage().window().setSize(new org.openqa.selenium.Dimension(375, 812));
//        }
//        return driver;
//    }
//
//    // Optional: Method to quit the driver
//    public static void quitDriver() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//        }
//    }
//}

