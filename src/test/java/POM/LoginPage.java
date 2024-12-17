package POM;

import CommonClasses.DriverManager;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    // Constructor to initialize WebDriver and page factory elements
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    // Locators for login page elements
    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(className = "ant-btn")
    WebElement loginButton;

    // Method to enter email
    public void enterEmail(String email) {
        emailField.sendKeys(email);
    }

    // Method to enter password
    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    // Method to click login button
    public void clickLoginButton() {
        loginButton.click();
    }

    //General login method that accepts role
    public void login_user(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

}
