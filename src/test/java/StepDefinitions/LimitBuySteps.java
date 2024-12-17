package StepDefinitions;

import CommonClasses.DriverManager;
import CommonClasses.ScenarioContext;
import POM.SpotPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import static org.testng.AssertJUnit.assertTrue;

public class LimitBuySteps {
    
    SpotPage buyerSpotPage;
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        buyerSpotPage = new SpotPage(driver);
    }

    @Given("the buyer navigates to the spot order placement page {string}")
    public void the_buyer_navigates_to_the_spot_order_placement_page(String carbonCredit) {
        String url = "http://localhost:5173/trade/spot/" + carbonCredit;
        driver.get(url);
    }

    @When("the buyer selects the order type")
    public void the_buyer_selects_the_order_type() {
        buyerSpotPage.selectLimitOrderType();
    }

    @Then("the buyer enters a limit buy price of {string}")
    public void the_buyer_enters_a_limit_buy_price_of(String price) {
        buyerSpotPage.enterBuyPrice(price);
        // Store price in shared context
        ScenarioContext.put("limitBuyPrice", price);
    }

    @And("the buyer enters a quantity of {string}")
    public void the_buyer_enters_a_quantity_of(String quantity) {
        buyerSpotPage.enterBuyQuantity(quantity);
        // Store quantity in shared context
        ScenarioContext.put("limitBuyQuantity", quantity);
    }

    @And("the buyer submits the order")
    public void the_buyer_submits_the_order() {
        buyerSpotPage.submitBuyOrder();
    }

    @And("the buy order should be placed successfully")
    public void the_buy_order_should_be_placed_successfully() {
        Assert.assertTrue(buyerSpotPage.isSuccessMessageDisplayed(), "Success message is not displayed");

        String expectedMessage = "Your order has been successfully placed";
        Assert.assertEquals(buyerSpotPage.getSuccessMessageText(), expectedMessage);
    }

    @And("the buyer info notification should be displayed")
    public void the_buyer_info_notification_should_be_displayed() {
        // Check if the notification is displayed
        Assert.assertTrue(buyerSpotPage.isInfoNotificationDisplayed(), "Info notification is not displayed");

        // Capture the actual notification text
        String actualNotification = buyerSpotPage.getInfoNotificationText();
        System.out.println("acutal : " + actualNotification);

        // Update the expected notification pattern to match either of the two possible patterns
        //String expectedNotificationPattern = "Your Spot Order T\\.[A-Za-z0-9]{8} of Type limit was partially filled \\((\\d{1,2}(\\.\\d{1,2})?)%\\)|Your Spot Order T\\.[0-9A-Z]{8} of Type limit was successfully filled";
        String expectedNotificationPattern = "Your Spot Order T\\.[A-Za-z0-9]{8} of Type (?i)limit(?-i) was partially filled \\((\\d{1,2}(\\.\\d{1,2})?)%\\)|Your Spot Order T\\.[A-Za-z0-9]{8} of Type (?i)limit(?-i) was successfully filled|Your Limit Order T\\.[A-Za-z0-9]{8} was automatically terminated by the system";

        // Assert that the actual notification matches the expected pattern
        Assert.assertTrue(actualNotification.matches(expectedNotificationPattern),
                "Notification does not match the expected pattern. Actual notification: " + actualNotification);
    }

    @When("the buyer cancels the partially filled open spot limit buy order")
    public void the_buyer_cancels_the_partially_filled_open_spot_limit_buy_order() throws InterruptedException {
        // Wait for 10 seconds
//        Thread.sleep(6000);

        buyerSpotPage.clickCancelAllButton();
        Assert.assertTrue(buyerSpotPage.isSuccessMessageDisplayed(), "Success message is not displayed");

        String expectedMessage = "Your order cancellation request to cancel all orders has been successfully placed";
        Assert.assertEquals(buyerSpotPage.getSuccessMessageText(), expectedMessage);
    }

    @Then("the partially filled spot order should be canceled successfully")
    public void the_partially_filled_spot_order_should_be_canceled_successfully() {
        // Refresh the current page
        driver.navigate().refresh();

        boolean ordersCanceled = buyerSpotPage.areAllOrdersCanceled();
        assertTrue("All orders should be canceled successfully, but some cancellations of spot orders of type Limit failed.", ordersCanceled);
    }

    @When("buyer cancels all open spot limit sell orders")
    public void buyer_cancels_all_open_spot_limit_sell_orders() throws InterruptedException {
        // Wait for 4 seconds
        Thread.sleep(8000);

        buyerSpotPage.clickCancelAllButton();
        Assert.assertTrue(buyerSpotPage.isSuccessMessageDisplayed(), "Success message is not displayed");

        String expectedMessage = "Your order cancellation request to cancel all orders has been successfully placed";
        Assert.assertEquals(buyerSpotPage.getSuccessMessageText(), expectedMessage);
    }

    @Then("buyer all orders should be canceled successfully")
    public void buyer_all_orders_should_be_canceled_successfully() {
        // Refresh the current page
        driver.navigate().refresh();

        boolean ordersCanceled = buyerSpotPage.areAllOrdersCanceled();
        assertTrue("All orders should be canceled successfully, but some cancellations of spot orders of type Limit failed.", ordersCanceled);
    }



}
