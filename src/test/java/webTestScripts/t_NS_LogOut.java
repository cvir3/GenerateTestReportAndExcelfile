package webTestScripts;

import org.testng.annotations.Test;
import utilities.baseClass;
import webFunctions.wf_Login;

public class t_NS_LogOut extends baseClass {

    @Test
    public void Logout() throws InterruptedException {
        wf_Login wl = new wf_Login(webDriver);
        wl.loginFunction();
        wl.logoutFunction();
    }
}
