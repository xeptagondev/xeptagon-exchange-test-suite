package POM;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MarketPage {
    WebDriver driver;

    @FindBy(xpath = "//div[text()='Top Carbon Credits by Market Capitalization']")
    WebElement txt_lbl;

    // Method to check if the market page is loaded
    public boolean is_load_market_page() {
        txt_lbl.isDisplayed();
        return true;
    }

    public MarketPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


}



