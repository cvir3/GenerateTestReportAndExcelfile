package utilities.ExtendReport;

import com.relevantcodes.extentreports.ExtentReports;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ExtentManager {

    private static ExtentReports extent;

    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            //Set date format
            DateFormat df = new SimpleDateFormat("dd_MMM_yyyy-hh_mm_ss_a");
            //Set HTML reporting file location
            extent = new ExtentReports("../GenerateTestReportAndExcelfile/Reports/Results_" + df.format(System.currentTimeMillis()) + ".html");
            extent.addSystemInfo("Environment", "DEV Environment");
            extent.loadConfig(new File("../GenerateTestReportAndExcelfile/src/test/java/utilites/ExtendReport/extent-config.xml"));
        }
        return extent;
    }
}
