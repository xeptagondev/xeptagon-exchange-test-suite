package StepDefinitions;

import BaseClasses.Base;
import CommonClasses.DriverManager;
import CommonClasses.ScenarioContext;

import POM.LoginPage;
import POM.MarketPage;
import POM.MyAccountPage;

import com.aventstack.extentreports.Status;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;


public class SellerAccountSteps extends Base {
    //Defined web driver
    private WebDriver driver;
    private LoginPage loginPage;
    private MarketPage marketPage;
    private MyAccountPage myAccountPage;

    // Static map to store account balances and credits
    public static Map<String, Double> accountBalances = new HashMap<>();
    public static Map<String, Double> creditsBalances = new HashMap<>();
    public Map<String, Double> initialAccountBalances;
    public Map<String, Double> initialCreditBalances;
    public Map<String, Double> newAccountBalances;
    public Map<String, Double> newCreditBalances;
    public String spotLimitBuyCommission;
    public String spotLimitSellCommission;

    // Class-level variable to store local storage value
    public Double totalTradeAmount;
    public Double totalBuyAmount;
    public Double remainBlockedAmount;
    // ExtentTest for logging test steps


//    // Constructor or dependency injection for initial account values
    public SellerAccountSteps() {
        // Retrieve initial balances from UserAccountBeforeSteps
        this.initialAccountBalances = new HashMap<>(accountBalances);
        this.initialCreditBalances = new HashMap<>(creditsBalances);
        this.newAccountBalances = new HashMap<>();
        this.newCreditBalances = new HashMap<>();

    }

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        myAccountPage = new MyAccountPage(driver);
        loginPage = new LoginPage(driver);
        marketPage = new MarketPage(driver);


    }

    @Given("seller logs into their account")
    public void seller_login_into_seller_account() {
        driver.get("http://localhost:5173/signIn");
        loginPage.enterEmail("shehans+EX2@xeptagon.com");  // Use valid email
        loginPage.enterPassword("EX2@xeptagon.coM");         // Use valid password
        loginPage.clickLoginButton();
        marketPage.is_load_market_page();

        // Log the result of login action


    }

    @When("seller navigate to the account page")
    public void seller_navigate_to_the_account_page() {
        // Use the provided account page URL
        driver.get("http://localhost:5173/myAssets/myAccount");
        logReport("Spot Limit Feature", Status.PASS, "seller navigate to the account page", true);
    }

    @When("seller retrieves the before account balances")
    public void seller_retrieves_the_before_account_balances() {
        driver.get("http://localhost:5173/myAssets/myAccount");

        // Retrieve balance values before any action
        accountBalances.put("grossBalance", myAccountPage.getGrossBalance());
        accountBalances.put("availableBalance", myAccountPage.getAvailableBalance());
        accountBalances.put("blockedAmount", myAccountPage.getBlockedBalance());
        logReport("Spot Limit Feature", Status.PASS, "seller retrieves the before account balances", true);
//        // Take screenshot
//        screenshotUtil.captureScreenshot("Before_Account_Balances");
//        ScreenshotUtil.takeScreenshot(driver, "account_balance_before");
    }

    @Then("seller retrieves the before credit balances for {string}")
    public void seller_retrieves_the_before_credit_balances_for(String carbonCredit) {

        // Retrieve credit balance for the specified carbon credit symbol before any action
        creditsBalances.put("totalCredits", myAccountPage.getTotalCredits(carbonCredit));
        creditsBalances.put("availableCredits", myAccountPage.getAvailableCredits(carbonCredit));
        creditsBalances.put("blockedCredits", myAccountPage.getBlockedCredits(carbonCredit));
        logReport("Spot Limit Cancelled Feature", Status.PASS, "seller retrieves the before credit balances for " + carbonCredit, true);
    }

    @Given("seller retrieve the after account balance")
    public void seller_retrieve_the_after_account_balance() {
        driver.get("http://localhost:5173/myAssets/myAccount");

        // Retrieve balance values after the buy order is placed
        newAccountBalances.put("grossBalance", myAccountPage.getGrossBalance());
        newAccountBalances.put("availableBalance", myAccountPage.getAvailableBalance());
        newAccountBalances.put("blockedAmount", myAccountPage.getBlockedBalance());

//        System.out.println("New Gross Balance: " + newAccountBalances.get("grossBalance"));
//        System.out.println("New Available Balance: " + newAccountBalances.get("availableBalance"));
//        System.out.println("New Blocked Amount: " + newAccountBalances.get("blockedAmount"));

    }

    @Given("seller retrieves the after credit balances {string}")
    public void seller_retrieves_the_after_credit_balances(String carbonCredit) {

        // Retrieve balance values after the buy order is placed)
        newCreditBalances.put("totalCredits", myAccountPage.getTotalCredits(carbonCredit));
        newCreditBalances.put("availableCredits", myAccountPage.getAvailableCredits(carbonCredit));
        newCreditBalances.put("blockedCredits", myAccountPage.getBlockedCredits(carbonCredit));

    }

    @And("seller validates that the gross balance has increased")
    public void seller_validate_that_the_gross_balance_has_increased() {
        // Cast WebDriver to JavascriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get commission value from local storage and store it in the class variable
        spotLimitSellCommission = (String) js.executeScript("return localStorage.getItem('spotLimitSellCommission');");

        spotLimitBuyCommission = (String) js.executeScript("return localStorage.getItem('spotLimitBuyCommission');");

        // Retrieve the stored price and quantity
        String sellPrice = (String) ScenarioContext.get("limitSellPrice");
        String sellQuantity = (String) ScenarioContext.get("limitSellQuantity");

        Double sellPriceValue = Double.parseDouble(sellPrice);
        Double sellQuantityValue = Double.parseDouble(sellQuantity);

        double LimitSellCommission = Double.parseDouble(spotLimitSellCommission);

        Double initialGrossBalance = initialAccountBalances.get("grossBalance");
        Double newGrossBalance = newAccountBalances.get("grossBalance");

        totalTradeAmount = (sellPriceValue * sellQuantityValue)  - ((sellPriceValue * sellQuantityValue) * LimitSellCommission / 100);
        double expectedGrossBalance = initialGrossBalance + (totalTradeAmount);


        Assert.assertEquals(newGrossBalance, expectedGrossBalance, 0.01, "Fiat currency gross balance should match the expected new gross balance after sell order");
    }

    @And("seller validates that the available balance has increased")
    public void seller_validate_that_the_available_balance_has_increased() {
        Double initialAvailableBalance = initialAccountBalances.get("availableBalance");
        Double newAvailableBalance = newAccountBalances.get("availableBalance");
        double expectedAvailableBalance = initialAvailableBalance + totalTradeAmount;

//        System.out.println("newAvailableBalance " + newAvailableBalance);
//        System.out.println("seller expectedAvailableBalance" + expectedAvailableBalance);

        Assert.assertEquals(newAvailableBalance, expectedAvailableBalance, 0.01, "Fiat currency available balance should match the expected new available balance after sell order");
    }

    @When("seller validate that the total credit balance has decreased")
    public void seller_validate_that_the_total_credit_has_decreased() {

        // Retrieve the stored price and quantity
        String price = (String) ScenarioContext.get("limitSellPrice");
        String quantity = (String) ScenarioContext.get("limitSellQuantity");
        Double SellQuantityValue = Double.parseDouble(quantity);

        // Validate that the total credit has decreased accordingly
        Double initialTotalCredits = initialCreditBalances.get("totalCredits");
        Double newTotalCredits = newCreditBalances.get("totalCredits");

        double expectedCreditBalance = initialTotalCredits - SellQuantityValue;
        Assert.assertEquals(newTotalCredits, expectedCreditBalance, 0.01, "Seller total credit balance should match the expected new total credit balance after sell order");

    }

    @Then("seller validate that the available credit balance has decreased")
    public void buyer_validate_that_the_available_credit_balance_has_decreased() {
        // Retrieve the stored price and quantity
        String quantity = (String) ScenarioContext.get("limitSellQuantity");
        Double SellQuantityValue = Double.parseDouble(quantity);

        // Validate that the total credit has decreased accordingly
        Double initialAvailableCredits = initialCreditBalances.get("availableCredits");
        Double newAvailableCredits = newCreditBalances.get("availableCredits");

        double expectedCreditBalance = initialAvailableCredits - SellQuantityValue;
        Assert.assertEquals(newAvailableCredits, expectedCreditBalance, 0.01, "Seller available credit balance should match the expected new available credit balance after sell order");

    }

    @Then("seller's available account balance should be unchanged from the initial available account balance")
    public void sellers_available_account_balance_should_be_unchanged_from_the_initial_available_account_balance() {
        // Logic to compare the current account balance with the initial account balance
        Double initialAvailableBalance = initialAccountBalances.get("availableBalance");
        Double newAvailableBalance = newAccountBalances.get("availableBalance");

//        System.out.println("seller newAvailableBalance " + newAvailableBalance);
//        System.out.println("seller expectedAvailableBalance " + initialAvailableBalance);

        Assert.assertEquals(newAvailableBalance, initialAvailableBalance, 0.01, "Fiat currency available balance should not be changed after canceled spot limit sell orders");
    }

    @Then("the available carbon credit balance should equal the initial credit balance")
    public void available_carbon_credit_balance_should_equal_the_initial_credit_balance() {
        // Logic to compare the available carbon credit balance with the initial credit balance

        // Validate that the total credit has decreased accordingly
        Double initialAvailableCredits = initialCreditBalances.get("availableCredits");
        Double newAvailableCredits = newCreditBalances.get("availableCredits");

        Assert.assertEquals(newAvailableCredits, initialAvailableCredits, 0.01, "Buyer available credit balance should not be changed after canceled spot limit sell orders");
//        System.out.println("initialAvailableCredits " + initialAvailableCredits);
//        System.out.println("newAvailableCredits " + newAvailableCredits);
    }

    @Then("the seller validates that the blocked balance has increased")
    public void seller_validate_that_the_blocked_balance_has_increased() {
        // Cast WebDriver to JavascriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get commission value from local storage and store it in the class variable
        spotLimitSellCommission = (String) js.executeScript("return localStorage.getItem('spotLimitSellCommission');");

        spotLimitBuyCommission = (String) js.executeScript("return localStorage.getItem('spotLimitBuyCommission');");


        // Retrieve the stored price and quantity
        //Here we use buy price and buy quantity because this step we use in terminated feature file.
        // Seller act as a buyer now for placed self matching order
        String buyPrice = (String) ScenarioContext.get("limitBuyPrice");
        String buyQuantity = (String) ScenarioContext.get("limitBuyQuantity");

        Double initialBlockedBalance = initialAccountBalances.get("blockedAmount");
        Double newBlockedAmount = newAccountBalances.get("blockedAmount");

        Double buyPriceValue = Double.parseDouble(buyPrice);
        Double buyQuantityValue = Double.parseDouble(buyQuantity);

        double buyCommission = Double.parseDouble(spotLimitBuyCommission);

        totalTradeAmount = (buyPriceValue * buyQuantityValue) + ((buyPriceValue * buyQuantityValue) * buyCommission / 100);
        double expectedBlockedBalance = initialBlockedBalance + totalTradeAmount;

//        System.out.println("sellCommission " + sellCommission);
//        System.out.println("totalAmount " + totalTradeAmount);

        Assert.assertEquals(newBlockedAmount, expectedBlockedBalance, 0.01, "Fiat currency blocked amount  should be increased");
    }

    @And("the seller validates that the available balance has decreased")
    public void seller_validate_that_the_available_balance_has_decreased() {

        Double initialAvailableBalance = initialAccountBalances.get("availableBalance");
        Double newAvailableBalance = newAccountBalances.get("availableBalance");
        double expectedAvailableBalance = initialAvailableBalance - totalTradeAmount;

//        System.out.println("newAvailableBalance " + newAvailableBalance);
//        System.out.println("seller expectedAvailableBalance" + expectedAvailableBalance);
        Assert.assertEquals(newAvailableBalance, expectedAvailableBalance, 0.01, "Fiat currency available balance should match the expected new available balance after sell order");
    }

    @Then("seller logout from application")
    public void buyer_logout_from_application() {
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        myAccountPage.clickPopIconButton();
        myAccountPage.clickLogOutButton();


    }

    // Method to take screenshot
    public void takeScreenshot(String stepName) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "screenshots/" + stepName + "_" + timestamp + ".png";
        try {
            FileUtils.copyFile(srcFile, new File(fileName));
            System.out.println("Screenshot captured: " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

}
