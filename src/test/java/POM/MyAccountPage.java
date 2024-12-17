package POM;

import CommonClasses.DriverManager;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MyAccountPage {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    // Constructor
    public MyAccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize Page Factory elements
    }

    // Locate the web elements using @FindBy
    @FindBy(css = "[data-test='gross-balance']")
    WebElement grossBalanceElement;

    @FindBy(css = "[data-test='available-balance']")
    WebElement availableBalanceElement;

    @FindBy(css = "[data-test='blocked-amount']")
    WebElement blockedAmountElement;

    @FindBy(css = "[data-test='pop-icon']")
    WebElement popIcon;

    // Locate the carbon credits table
    @FindBy(css = "[data-test='carbon-credits-table']")
    WebElement carbonCreditsTable;

    // Locate all carbon credit rows (symbols)
    @FindBy(css = "[data-test='carbon-credit']")
    List<WebElement> carbonCreditSymbols;

    // Locate the available Quantity, last price, and balance columns
    @FindBy(css = "[data-test='available-quantity']")
    List<WebElement> availableQuantityCells;

    @FindBy(css = "[data-test='total-quantity']")
    List<WebElement> totalQuantityCells;

    @FindBy(css = "[data-test='blocked-quantity']")
    List<WebElement> blockedQuantity;

    @FindBy(css = "[data-test='last-price']")
    List<WebElement> lastPriceCells;

    @FindBy(css = "[data-test='logout-btn']")
    WebElement logoutBtn;


    // Method to get gross balance
    public double getGrossBalance() {
        return Double.parseDouble(grossBalanceElement.getText().replace(",", ""));
    }

    // Method to get available balance
    public double getAvailableBalance() {
        return Double.parseDouble(availableBalanceElement.getText().replace(",", ""));
    }

    // Method to get blocked amount
    public double getBlockedBalance() {
        return Double.parseDouble(blockedAmountElement.getText().replace(",", ""));
    }

    // Click the pop icon button
    public void clickPopIconButton() {
        popIcon.click();
    }

    // Click the logout button
    public void clickLogOutButton(){
        logoutBtn.click();
    }

    // Method to get credit balance for a specific symbol
    public double getTotalCredits(String symbol) {
        for (int i = 0; i < carbonCreditSymbols.size(); i++) {
            if (carbonCreditSymbols.get(i).getText().equals(symbol)) {
                // Assuming balance information is in the same row as the symbol
                return Double.parseDouble(totalQuantityCells.get(i).getText().replace(",", ""));
            }
        }
        return 0.0; // Return 0.0 if the symbol is not found
    }

    // Method to get available quantity for a specific symbol
    public double getAvailableCredits(String symbol) {
        for (int i = 0; i < carbonCreditSymbols.size(); i++) {
            if (carbonCreditSymbols.get(i).getText().equals(symbol)) {
                return Double.parseDouble(availableQuantityCells.get(i).getText().replace(",", ""));
            }
        }
        return 0.0; // Return 0.0 if the symbol is not found
    }

    // Method to get blocked credit quantity for a specific symbol
    public double getBlockedCredits(String symbol) {
        for (int i = 0; i < carbonCreditSymbols.size(); i++) {
            if (carbonCreditSymbols.get(i).getText().equals(symbol)) {
                return Double.parseDouble(blockedQuantity.get(i).getText().replace(",", ""));
            }
        }
        return 0.0; // Return 0.0 if the symbol is not found
    }

    // Method to get last price for a specific symbol
    public double getLastPriceForSymbol(String symbol) {
        for (int i = 0; i < carbonCreditSymbols.size(); i++) {
            if (carbonCreditSymbols.get(i).getText().equals(symbol)) {
                return Double.parseDouble(lastPriceCells.get(i).getText().replace(",", ""));
            }
        }
        return 0.0; // Return 0.0 if the symbol is not found
    }



}
