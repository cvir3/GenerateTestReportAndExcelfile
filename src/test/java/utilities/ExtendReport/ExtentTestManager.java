package utilities.ExtendReport;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    static Map extentTestMap = new HashMap();
    static ExtentReports extent = ExtentManager.getReporter();

    public ExtentTestManager() {
    }

    public static synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized void endTest() {
        extent.endTest((ExtentTest) extentTestMap.get((int) Thread.currentThread().getId()));
    }

    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.startTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }
}
