package webFunctions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utilities.baseClass;
import webElements.we_Login;
import com.relevantcodes.extentreports.LogStatus;
import utilities.ExtendReport.ExtentTestManager;

public class wf_Login extends baseClass {
    WebDriver webDriver;
    we_Login wE = new we_Login();

    /* This is Constructor */
    public wf_Login(WebDriver remoteDriver) {
        webDriver = remoteDriver;
    }

    // This is negative scenario
    public void invalidLoginFunction() throws InterruptedException {
        webDriver.findElement(wE.inputUserName).sendKeys("testt");
        webDriver.findElement(wE.inputPassword).sendKeys("admin123");
        webDriver.findElement(wE.btnLogin).click();
        WebElement Invalidcredtext = webDriver.findElement(wE.Invalidcred);
        String expectedValue = Invalidcredtext.getText();
        String actualValue = "Invalid credentials";
        if (actualValue.equals(expectedValue)) {
            System.out.println("Test Pass " + actualValue);
        } else {
            System.out.println("Test fail " + expectedValue + ", but found: " + actualValue);
            Assert.assertFalse(true, "Test is fail");
            //The phrase "but found" is used to indicate a comparison between an expected value and an actual value.
        }
        webDriver.get(webDriver.getCurrentUrl());
        Thread.sleep(1000);
    }

    public void loginFunction() throws InterruptedException {
        WebElement username = webDriver.findElement(wE.inputUserName);
        username.isDisplayed();
        username.isEnabled();
        username.sendKeys("Admin");
        WebElement password = webDriver.findElement(wE.inputPassword);
        password.isDisplayed();
        password.isEnabled();
        password.sendKeys("admin123");
        webDriver.findElement(wE.btnLogin).click();
        String url = webDriver.getCurrentUrl();
        Assert.assertEquals(url, "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index");
        Thread.sleep(1000);
    }

    public void logoutFunction() throws InterruptedException {
        webDriver.findElement(wE.selectUserProfile).click();
        Thread.sleep(3000);
        webDriver.findElement(wE.clickOnLogout).click();
        Thread.sleep(3000);
        String url = webDriver.getCurrentUrl();
        Assert.assertEquals(url, "https://opensource-demo.orangehrmlive.com/web/index.php/auth/loginm");
    }
}
