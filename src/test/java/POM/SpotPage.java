package POM;

import BaseClasses.Base;
import io.cucumber.java.en.But;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.prefs.BackingStoreException;

public class SpotPage extends Base {

    private final WebDriverWait wait;
    private WebDriver driver;

//    @Before
//    public void setUp() {
//        driver = DriverManager.getDriver();
//    }

    public SpotPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[data-test='limit-order-btn']")
    WebElement limitOrderType;

    @FindBy(css = "[data-test='market-order-btn']")
    WebElement marketOrderType;

    @FindBy(css = "[data-test='sell-price']")
    WebElement sellPriceInput;

    @FindBy(css = "[data-test='sell-quantity']")
    WebElement sellQuantityInput;

    @FindBy(css = "[data-test='sell-btn']")
    WebElement submitSellButton;

    @FindBy(css = "[data-test='success-notification']")
    WebElement successMessage;

    @FindBy(css = "[data-test='info-notification']")
    WebElement infoNotification;

    @FindBy(css = "[data-test='cancel-all-btn']")
    WebElement cancelAllButton;

    // Locate all order id rows
    @FindBy(css = "[data-test='open-order-id']")
    List<WebElement> openOrderIds;

    @FindBy(css = "[data-test='buy-price']")
    WebElement buyPriceInput;

    @FindBy(css = "[data-test='buy-quantity']")
    WebElement buyQuantityInput;

    @FindBy(css = "[data-test='buy-btn']")
    WebElement submitBuyButton;

    public void selectLimitOrderType() {
        limitOrderType.click();
    }

//    public void enterBuyPrice(String price) {
//        wait.until(ExpectedConditions.elementToBeClickable(buyPriceInput)); // Wait for element to be clickable
//        buyPriceInput.click();
////        buyPriceInput.clear();
//        buyPriceInput.sendKeys(price);
//    }
//
//    public void enterBuyQuantity(String quantity) {
//        wait.until(ExpectedConditions.elementToBeClickable(buyQuantityInput)); // Wait for element to be clickable
//        buyQuantityInput.click();
////        buyQuantityInput.clear();
//        buyQuantityInput.sendKeys(quantity);
//    }
//
//    public void enterSellPrice(String price) {
//        wait.until(ExpectedConditions.elementToBeClickable(sellPriceInput));
//        sellPriceInput.click();
////        sellPriceInput.clear();
//        sellPriceInput.sendKeys(price);
//    }
//
//    public void enterSellQuantity(String quantity) {
//        wait.until(ExpectedConditions.elementToBeClickable(sellQuantityInput));
//        sellQuantityInput.click();
////        sellQuantityInput.clear();
//        sellQuantityInput.sendKeys(quantity);
//    }

    // Retry click logic to handle stale elements
    public void retryingClick(WebElement element) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)); // Wait for the element to be clickable
                element.click();  // Click the element
                break;  // Exit the loop if the click is successful
            } catch (StaleElementReferenceException e) {
                System.out.println("Attempt " + (attempts + 1) + ": Retrying click due to stale element.");
            }
            attempts++;
        }
    }

    public void enterBuyPrice(String price) {
        retryingClick(buyPriceInput);
        buyPriceInput.clear();
        buyPriceInput.sendKeys(price);
    }

    public void enterBuyQuantity(String quantity) {
        retryingClick(buyQuantityInput);
        buyQuantityInput.clear();
        buyQuantityInput.sendKeys(quantity);
    }

    public void enterSellPrice(String price) {
        retryingClick(sellPriceInput);
        sellPriceInput.clear();
        sellPriceInput.sendKeys(price);
    }

    public void enterSellQuantity(String quantity) {
        retryingClick(sellQuantityInput);
        sellQuantityInput.clear();
        sellQuantityInput.sendKeys(quantity);
    }

    public void submitBuyOrder() {
        submitBuyButton.click();
    }

    public void submitSellOrder() {
        submitSellButton.click();
    }

    // Updated method to check if the success message is displayed with WebDriverWait
    public boolean isSuccessMessageDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            return successMessage.isDisplayed();
        } catch (Exception e) {
            return false; // Return false if the message does not appear within 3 seconds
        }
    }

    public String getSuccessMessageText() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        return successMessage.getText();
    }

    @FindBy(css = "[data-test='cancel-now']")
    WebElement cancelNowButton;

    public void clickCancelAllButton(){
        cancelAllButton.click();
        cancelNowButton.click();


    }

//    public void clickNotification(){
//        infoNotification.click();
//    }

    public boolean areAllOrdersCanceled() {
        return openOrderIds.isEmpty(); // Returns true if no open order IDs are found
    }

    public boolean isInfoNotificationDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        try {
            wait.until(ExpectedConditions.visibilityOf(infoNotification));
            return infoNotification.isDisplayed();
        } catch (Exception e) {
            return false; // Return false if the message does not appear within 3 seconds
        }
    }

    public String getInfoNotificationText() {
        return infoNotification.getText();
    }



}
