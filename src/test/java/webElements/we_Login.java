package webElements;
import org.openqa.selenium.By;

public class we_Login {

    public By inputUserName = By.xpath("//input[@name='username']");
    public By inputPassword = By.xpath("//input[@name='password']");
    public By btnLogin = By.xpath("//button[normalize-space()='Login']");
    public By selectUserProfile = By.xpath("//i[@class='oxd-icon bi-caret-down-fill oxd-userdropdown-icon']");
    public By clickOnLogout = By.xpath("//a[normalize-space()='Logout']");
    public By Invalidcred = By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']");

}

