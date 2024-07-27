package webTestScripts;
import org.testng.annotations.*;
import utilities.baseClass;
import webFunctions.wf_Login;

import java.io.IOException;

public class t_NS_Login extends baseClass {

    @Test
    public void Login() throws InterruptedException {
        wf_Login wl = new wf_Login(webDriver);
        wl.loginFunction();
    }
}
