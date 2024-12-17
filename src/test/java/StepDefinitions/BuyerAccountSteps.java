package StepDefinitions;

import CommonClasses.DriverManager;
import CommonClasses.ScenarioContext;
import POM.LoginPage;
import POM.MarketPage;
import POM.MyAccountPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class BuyerAccountSteps {
    //Defined web driver
    private WebDriver driver;

    //Initiate objects
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

//    public Double totalCredits;
//    public Double blockedCredits;
//    public Double availableCredits;
//
//    public Double grossBalance;
//    public Double blockedBalance;
//    public Double availableBalance;

    public Double totalTradeAmount;
    public Double totalBuyAmount;
    public Double remainBlockedAmount;

    public BuyerAccountSteps() {
        // Retrieve initial balances from BuyerAccBeforeStep
        this.initialAccountBalances = new HashMap<>(accountBalances);
        this.initialCreditBalances = new HashMap<>(creditsBalances);
        this.newAccountBalances = new HashMap<>();
        this.newCreditBalances = new HashMap<>();
    }

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        myAccountPage = new MyAccountPage(driver);
    }

    @Given("buyer navigate to the account page")
    public void buyer_navigate_to_the_account_page() {
        // Use the provided account page URL
        driver.get("http://localhost:5173/myAssets/myAccount");
    }

    @When("buyer retrieves the before account balances")
    public void buyer_retrieves_the_before_account_balances() {
        driver.get("http://localhost:5173/myAssets/myAccount");

        // Retrieve balance values before any action
        accountBalances.put("grossBalance", myAccountPage.getGrossBalance());
        accountBalances.put("availableBalance", myAccountPage.getAvailableBalance());
        accountBalances.put("blockedAmount", myAccountPage.getBlockedBalance());
    }

    @Then("buyer retrieves the before credit balances for {string}")
    public void buyer_retrieves_the_before_credit_balances_for(String carbonCredit) {

        // Retrieve credit balance for the specified carbon credit symbol before any action
        creditsBalances.put("totalCredits", myAccountPage.getTotalCredits(carbonCredit));
        creditsBalances.put("availableCredits", myAccountPage.getAvailableCredits(carbonCredit));
        creditsBalances.put("blockedCredits", myAccountPage.getBlockedCredits(carbonCredit));
    }


    @Given("buyer retrieve the after account balance")
    public void buyer_retrieve_the_after_account_balance() {
        driver.get("http://localhost:5173/myAssets/myAccount");

        // Retrieve balance values after the buy order is placed
        newAccountBalances.put("grossBalance", myAccountPage.getGrossBalance());
        newAccountBalances.put("availableBalance", myAccountPage.getAvailableBalance());
        newAccountBalances.put("blockedAmount", myAccountPage.getBlockedBalance());

//        System.out.println("New Gross Balance: " + newAccountBalances.get("grossBalance"));
//        System.out.println("New Available Balance: " + newAccountBalances.get("availableBalance"));
//        System.out.println("New Blocked Amount: " + newAccountBalances.get("blockedAmount"));

    }

    @Given("buyer retrieves the after credit balances {string}")
    public void buyer_retrieves_the_after_credit_balances(String carbonCredit) {

        // Retrieve balance values after the buy order is placed)
        newCreditBalances.put("totalCredits", myAccountPage.getTotalCredits(carbonCredit));
        newCreditBalances.put("availableCredits", myAccountPage.getAvailableCredits(carbonCredit));
        newCreditBalances.put("blockedCredits", myAccountPage.getBlockedCredits(carbonCredit));

    }

    @When("buyer validate that the gross balance has decreased")
    public void buyer_validate_that_the_gross_balance_has_decreased() {
        // Cast WebDriver to JavascriptExecutor
        // Cast WebDriver to JavascriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get commission value from local storage and store it in the class variable
        spotLimitSellCommission = (String) js.executeScript("return localStorage.getItem('spotLimitSellCommission');");

        spotLimitBuyCommission = (String) js.executeScript("return localStorage.getItem('spotLimitBuyCommission');");

        // Retrieve the stored price and quantity
        String buyPrice = (String) ScenarioContext.get("limitBuyPrice");
        String buyQuantity = (String) ScenarioContext.get("limitBuyQuantity");
        String sellPrice = (String) ScenarioContext.get("limitSellPrice");
        String sellQuantity = (String) ScenarioContext.get("limitSellQuantity");

        Double buyPriceValue = Double.parseDouble(buyPrice);
        Double sellQuantityValue = Double.parseDouble(sellQuantity);
        Double buyQuantityValue = Double.parseDouble(buyQuantity);
        double LimitBuyCommission = Double.parseDouble(spotLimitBuyCommission);

        Double initialGrossBalance = initialAccountBalances.get("grossBalance");
        Double newGrossBalance = newAccountBalances.get("grossBalance");

        totalTradeAmount = (buyPriceValue * sellQuantityValue)  + ((buyPriceValue * sellQuantityValue) * LimitBuyCommission / 100);
        double expectedGrossBalance = initialGrossBalance - (totalTradeAmount);

//        System.out.println("Limit Buy Price: " + buyPrice);
//        System.out.println("Limit Buy Quantity: " + buyQuantity);
//        System.out.println("Limit sell Price: " + sellPrice);
//        System.out.println("Limit sell Quantity: " + sellQuantity);
//        System.out.println("totalAmount: " + totalTradeAmount);
//        System.out.println("expectedGrossBalance: " + expectedGrossBalance);
//        System.out.println("initialGrossBalance: " + initialGrossBalance);
        Assert.assertEquals(newGrossBalance, expectedGrossBalance, 0.01,"Fiat currency gross balance should match the expected new gross balance after buy order");

    }

    @Then("buyer validate that the available balance has decreased")
    public void buyer_validate_that_the_available_balance_has_decreased() {
        // Retrieve the stored price and quantity
        String buyPrice = (String) ScenarioContext.get("limitBuyPrice");
        String buyQuantity = (String) ScenarioContext.get("limitBuyQuantity");
        String sellPrice = (String) ScenarioContext.get("limitSellPrice");
        String sellQuantity = (String) ScenarioContext.get("limitSellQuantity");

        Double initialAvailableBalance = initialAccountBalances.get("availableBalance");
        Double newAvailableBalance = newAccountBalances.get("availableBalance");

        Double BuyPriceValue = Double.parseDouble(buyPrice);
        Double sellQuantityValue = Double.parseDouble(sellQuantity);
        Double buyQuantityValue = Double.parseDouble(buyQuantity);
        double buyCommission = Double.parseDouble(spotLimitBuyCommission);

        totalTradeAmount = (BuyPriceValue * sellQuantityValue)  + ((BuyPriceValue * sellQuantityValue) * buyCommission / 100);
        totalBuyAmount = (BuyPriceValue * buyQuantityValue)  + ((BuyPriceValue * buyQuantityValue) * buyCommission / 100);

        //In here use totalBuyAmount because when partially filled limit order we still have blocked amount
        // which mean total buy amount need to be reduced from initial available balance
        double expectedAvailableBalance = initialAvailableBalance - (totalBuyAmount);

//        System.out.println("Limit Buy Price: " + buyPrice);
//        System.out.println("Limit Buy Quantity: " + buyQuantity);
//        System.out.println("Limit sell Price: " + sellPrice);
//        System.out.println("Limit sell Quantity: " + sellQuantity);
//        System.out.println("totalAmount: " + totalTradeAmount);
//        System.out.println("totalBuyAmount: " + totalBuyAmount);
//        System.out.println("newAvailableBalance" + newAvailableBalance);
//        System.out.println("expectedAvailableBalance" + expectedAvailableBalance);
//        System.out.println("initialAvailableBalance" + initialAvailableBalance);

        Assert.assertEquals(newAvailableBalance, expectedAvailableBalance, 0.01,"Fiat currency available balance should match the expected new available balance after buy order");
    }

    @And("buyer validate that the block amount has increased")
    public void buyer_validate_that_the_block_amount_has_increased() {
        // Retrieve the stored price and quantity
        String buyPrice = (String) ScenarioContext.get("limitBuyPrice");
        String buyQuantity = (String) ScenarioContext.get("limitBuyQuantity");
        String sellPrice = (String) ScenarioContext.get("limitSellPrice");
        String sellQuantity = (String) ScenarioContext.get("limitSellQuantity");

        Double initialBlockedAmount = initialAccountBalances.get("blockedAmount");
        Double newBlockedAmount = newAccountBalances.get("blockedAmount");

        double buyPriceValue = Double.parseDouble(buyPrice);
        Double sellPriceValue = Double.parseDouble(sellPrice);
        Double SellQuantityValue = Double.parseDouble(sellQuantity);
        Double BuyQuantityValue = Double.parseDouble(buyQuantity);
        double buyCommission = Double.parseDouble(spotLimitBuyCommission);

        //remainBlockedAmount = ((buyPriceValue * BuyQuantityValue)  - (sellPriceValue * SellQuantityValue)) * buyCommission / 100;
        remainBlockedAmount = (BuyQuantityValue-SellQuantityValue) * buyPriceValue * (1 + buyCommission/100);
        ScenarioContext.put("remainBlockedAmount", remainBlockedAmount);

        double expectedBlockedAmount = initialBlockedAmount + (remainBlockedAmount);

//        System.out.println("Limit Buy Price: " + buyPrice);
//        System.out.println("Limit Buy Quantity: " + buyQuantity);
//        System.out.println("Limit sell Price: " + sellPrice);
//        System.out.println("Limit sell Quantity: " + sellQuantity);
//        System.out.println("remainBlockedAmount: " + remainBlockedAmount);
//        System.out.println("expectedBlockedAmount: " + expectedBlockedAmount);
//        System.out.println("initialBlockedAmount: " + initialBlockedAmount);
        Assert.assertEquals(newBlockedAmount, expectedBlockedAmount, 0.01,"Fiat currency blocked amount should match the expected blocked amount after buy order");

    }

    @When("buyer validate that the total credit balance has increased")
    public void buyer_validate_that_the_total_credit_has_increased() {
        // Retrieve the stored price and quantity
        String quantity = (String) ScenarioContext.get("limitSellQuantity");
        Double quantityValue = Double.parseDouble(quantity);

        // Validate that the total credit has decreased accordingly
        Double initialTotalCredits = initialCreditBalances.get("totalCredits");
        Double newTotalCredits = newCreditBalances.get("totalCredits");

//        System.out.println("initialTotalCredits " + initialTotalCredits);
//        System.out.println("newTotalCredits " + newTotalCredits);

        double expectedCreditBalance = initialTotalCredits + quantityValue;
        Assert.assertEquals(newTotalCredits, expectedCreditBalance, 0.01, "Buyer total credit balance should match the expected new total credit balance after buy order");

    }

    @Then("buyer validate that the available credit balance has increased")
    public void buyer_validate_that_the_available_credit_balance_has_increased() {
        // Retrieve the stored price and quantity
        String quantity = (String) ScenarioContext.get("limitSellQuantity");    //in partially filled scenario we trade sell Q=2 with buy Q=7, so we need to use actual trade quantity which mean Q=2, this can also use full filed scenario.
        Double quantityValue = Double.parseDouble(quantity);

        // Validate that the total credit has decreased accordingly
        Double initialAvailableCredits = initialCreditBalances.get("availableCredits");
        Double newAvailableCredits = newCreditBalances.get("availableCredits");

        double expectedCreditBalance = initialAvailableCredits + quantityValue;
        Assert.assertEquals(newAvailableCredits, expectedCreditBalance, 0.01, "Buyer available credit balance should match the expected new available credit balance after buy order");
//        System.out.println("initialAvailableCredits " + initialAvailableCredits);
//        System.out.println("newAvailableCredits " + newAvailableCredits);
    }

    @And("buyer retrieve the new buyer account balance after cancelling")
    public void buyer_retrieve_the_new_buyer_account_balance_after_cancelling() {
        driver.get("http://localhost:5173/myAssets/myAccount");

        // Cast WebDriver to JavascriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get value from local storage and store it in the class variable
        spotLimitBuyCommission = (String) js.executeScript("return localStorage.getItem('spotLimitBuyCommission');");

        // Retrieve balance values after the limit buy order is placed
        newAccountBalances.put("grossBalanceAfterCancel", myAccountPage.getGrossBalance());
        newAccountBalances.put("availableBalanceAfterCancel", myAccountPage.getAvailableBalance());
        newAccountBalances.put("blockedAmountAfterCancel", myAccountPage.getBlockedBalance());
//        System.out.println("New Gross Balance AfterCancel : " + newAccountBalances.get("grossBalanceAfterCancel"));
//        System.out.println("New Available Balance AfterCancel : " + newAccountBalances.get("availableBalanceAfterCancel"));
//        System.out.println("New Blocked Amount AfterCancel  : " + newAccountBalances.get("blockedAmountAfterCancel"));
    }

    @Given("buyer retrieve the new buyer account balance after partially terminated")
    public void buyer_retrieve_the_new_buyer_account_balance_after_partially_terminated() {
        driver.get("http://localhost:5173/myAssets/myAccount");

        // Cast WebDriver to JavascriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Get value from local storage and store it in the class variable
        spotLimitBuyCommission = (String) js.executeScript("return localStorage.getItem('spotLimitBuyCommission');");

        // Retrieve balance values after the limit buy order is placed
        newAccountBalances.put("grossBalanceAfterPartiallyTerminated", myAccountPage.getGrossBalance());
        newAccountBalances.put("availableBalanceAfterPartiallyTerminated", myAccountPage.getAvailableBalance());
        newAccountBalances.put("blockedAmountAfterPartiallyTerminated", myAccountPage.getBlockedBalance());
//        System.out.println("New Gross Balance AfterCancel : " + newAccountBalances.get("grossBalanceAfterCancel"));
//        System.out.println("New Available Balance AfterCancel : " + newAccountBalances.get("availableBalanceAfterCancel"));
//        System.out.println("New Blocked Amount AfterCancel  : " + newAccountBalances.get("blockedAmountAfterCancel"));
    }

    @And("buyer fiat currency block amount should be released and available balance increased")
    public void buyer_fiat_currency_block_amount_should_be_released_and_available_balance_increased() {

        Double remainBlockedAmount = (Double) ScenarioContext.get("remainBlockedAmount");

        Double initialBlockedBalance = initialAccountBalances.get("blockedAmount");
        Double newAccountBalance = newAccountBalances.get("availableBalance");

        Double availableBalanceAfterCancel = newAccountBalances.get("availableBalanceAfterCancel");
        Double blockedAmountAfterCancel = newAccountBalances.get("blockedAmountAfterCancel");

        double expectedAccountBalance = newAccountBalance + remainBlockedAmount;

        Assert.assertEquals(blockedAmountAfterCancel, initialBlockedBalance, 0.01,"Fiat currency blocked amount should not be changed after cancelled spot limit buy partially filled order");
        Assert.assertEquals(availableBalanceAfterCancel, expectedAccountBalance, 0.01,"Fiat currency initial available balance amount should be equal to new available balance after cancelled spot limit buy partially filled order");

    }

    @When("buyer fiat currency block amount should be released and available balance increased after partially terminated")
    public void buyer_fiat_currency_block_amount_should_be_released_and_available_balance_increased_after_partially_terminated() {

        Double remainBlockedAmount = (Double) ScenarioContext.get("remainBlockedAmount");

        Double initialBlockedBalance = initialAccountBalances.get("blockedAmount");
        Double newAccountBalance = newAccountBalances.get("availableBalance");

        Double availableBalanceAfterPartiallyTerminated = newAccountBalances.get("availableBalanceAfterPartiallyTerminated");
        Double blockedAmountAfterPartiallyTerminated = newAccountBalances.get("blockedAmountAfterPartiallyTerminated");

        double expectedAccountBalance = newAccountBalance + remainBlockedAmount;

        Assert.assertEquals(blockedAmountAfterPartiallyTerminated, initialBlockedBalance, 0.01,"Fiat currency blocked amount should not be changed after cancelled spot limit buy partially filled order");
        Assert.assertEquals(availableBalanceAfterPartiallyTerminated, expectedAccountBalance, 0.01,"Fiat currency initial available balance amount should be equal to new available balance after cancelled spot limit buy partially filled order");

    }

    @Then("buyer logout from application")
    public void buyer_logout_from_application() {
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        myAccountPage.clickPopIconButton();
        myAccountPage.clickLogOutButton();


    }


}
