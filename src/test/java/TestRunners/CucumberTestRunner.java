package TestRunners;
//import io.cucumber.testng.AbstractTestNGCucumberTests;
//import io.cucumber.testng.CucumberOptions;
//
//@CucumberOptions(
//        features = {
//               "src/test/resources/spot/01_Spot_Limit_Filled.feature",
////                "src/test/resources/spot/02_Spot_Limit_Cancelled.feature",
////                "src/test/resources/spot/03_Spot_Limit_Partially_Filled_and_Cancelled.feature",
////                "src/test/resources/spot/04_Spot_Limit_Terminated.feature",
////                "src/test/resources/spot/05_Spot_Limit_Partially_Terminated.feature",
//
//        },// Path to feature files
//
//        glue = {"StepDefinitions", "hooks"}, // Path to step definition classes
//        plugin = {
//                "pretty", // Output format for console
//                "html:target/cucumber-reports.html" // HTML report path
//        },
//        tags = "" // Tags for filtering scenarios
//)
//public class CucumberTestRunner extends AbstractTestNGCucumberTests {
//    // You can add additional configurations here if needed
//}


import BaseClasses.Base;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

@CucumberOptions(
        features = {
                "src/test/resources/spot/01_Spot_Limit_Filled.feature", // Path to feature files
//                "src/test/resources/spot/02_Spot_Limit_Cancelled.feature",
//                "src/test/resources/spot/03_Spot_Limit_Partially_Filled_and_Cancelled.feature",
//                "src/test/resources/spot/04_Spot_Limit_Terminated.feature",
//                "src/test/resources/spot/05_Spot_Limit_Partially_Terminated.feature",
        },
        glue = {"StepDefinitions"}, // Path to step definition classes
        plugin = {
                "pretty", // Console output format
                "html:target/cucumber-reports.html", // HTML report path
                "json:target/cucumber-reports/cucumber.json" // JSON report for additional integrations
        },
        tags = "" // Tags for filtering scenarios
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

        @BeforeClass
        public void setupExtentReports() {
                // Call the report generation setup from the Base class
                Base base = new Base();
                base.generateReport();
        }

        @AfterClass
        public void tearDownExtentReports() {
                // Flush the Extent Reports after all tests are executed
                Base base = new Base();
                base.closeReport();
        }
}