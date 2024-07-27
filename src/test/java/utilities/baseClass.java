package utilities;

import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utilities.ExtendReport.ExtentManager;
import utilities.ExtendReport.ExtentTestManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

public class baseClass {
    public static WebDriver webDriver;
    private SimpleDateFormat df = new SimpleDateFormat("dd_MMM_yyyy-hh_mm_ss_a");

    /*---This is cross browser code---*/
    @BeforeClass(alwaysRun = true)
    public void setup() {
        String browser = "chrome"; // Change this to the browser you want to test
        initialize(browser);
        webDriver.manage().window().maximize();
        webDriver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        System.out.println("Page title is " + webDriver.getTitle());
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    }

    public void initialize(String browser) {
        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            webDriver = new FirefoxDriver(firefoxOptions);
            System.out.println("Firefox is launched");
        } else if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--start-maximized");
            webDriver = new ChromeDriver(chromeOptions);
            System.out.println("Chrome is launched");
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions edgeOptions = new EdgeOptions();
            webDriver = new EdgeDriver(edgeOptions);
            System.out.println("Edge is launched");
        }
    }

    public String getCurrentPageUrl() {
        return webDriver.getCurrentUrl();
    }

    public static void takeSnapShot(WebDriver driver, String fileWithPath) throws Exception {
        TakesScreenshot scrShot = (TakesScreenshot) driver;
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(SrcFile, DestFile);
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        ExtentTestManager.startTest("Method Name: " + method.getName());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("TEST STARTED # " + method.getAnnotation(Test.class).description());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    @AfterMethod
    public void afterMethod(ITestResult result, Method method) throws Exception {
        String fileName;
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String testSRes;
        Date endDateTime;
        String testDescription;
        String dateEndInString;
        if (result.getStatus() == 2) {
            endDateTime = new Date();
            DateFormat shortDf = DateFormat.getDateTimeInstance(3, 3);
            testDescription = shortDf.format(endDateTime).replace("/", "_");
            testDescription = testDescription.replace(" ", "_");
            testDescription = testDescription.replace(":", "_");
            dateEndInString = "SC_error__" + testDescription;
            fileName = System.getProperty("user.dir") + "/Reports/Failure_Screenshots/" + dateEndInString + ".png";
            takeSnapShot(webDriver, fileName);
            ExtentTestManager.getTest().log(LogStatus.FAIL, "Error Screenshot" + ExtentTestManager.getTest().addScreenCapture("failure_screenshots\\" + dateEndInString + ".png"));
            ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable().getMessage());
            ExtentTestManager.getTest().log(LogStatus.FAIL, "Test Failed");
            testSRes = "FAIL";
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
            testSRes = "SKIP";
        } else {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
            testSRes = "PASS";
        }

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("TEST COMPLETED # [ " + testSRes + " ] " + method.getAnnotation(Test.class).description());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
        ExtentManager.getReporter().flush();
        ExtentTestManager.getTest().log(LogStatus.INFO, "Closed");
        Thread.sleep(2000);
    }



    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (webDriver != null) {
            try {
                File mostRecentReport = getMostRecentReportFile("../GenerateTestReportAndExcelfile/Reports");
                if (mostRecentReport != null) {
                    webDriver.get(mostRecentReport.toURI().toString());
                    createExcel();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                webDriver.quit();
            }
        }
    }

    public static File getMostRecentReportFile(String reportFolderPath) throws IOException {
        Path reportFolder = Paths.get(reportFolderPath);

        Optional<File> mostRecentFile = Files.list(reportFolder)
                .filter(Files::isRegularFile)
                .max(Comparator.comparingLong(path -> path.toFile().lastModified()))
                .map(Path::toFile);

        if (mostRecentFile.isPresent()) {
            return mostRecentFile.get();
        } else {
            throw new IOException("No report files found in the specified folder");
        }
    }
    public void createExcel() throws IOException {
        // Navigate to the report
        webDriver.findElement(By.xpath("//a[normalize-space()='Analysis']")).click();
        // Extract test data
        String totalTests = webDriver.findElement(By.cssSelector("span[class='total-tests'] span[class='panel-lead']")).getText();
        String totalSteps = webDriver.findElement(By.cssSelector("span[class='total-steps'] span[class='panel-lead']")).getText();
        String totalPass = webDriver.findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(4) > span:nth-child(1)")).getText();
        String totalFailed = webDriver.findElement(By.xpath("//span[contains(text(),'test(s) failed,')]")).getText();
        String totalPercentage = webDriver.findElement(By.cssSelector(".pass-percentage.panel-lead")).getText();
        String startTime = webDriver.findElement(By.cssSelector(".panel-lead.suite-started-time")).getText();
        String endTime = webDriver.findElement(By.cssSelector(".panel-lead.suite-ended-time")).getText();


        // Define the Excel file path
        String excelFilePath = "src/main/resources/TestReport.xlsx"; // Corrected the path

        // Read the existing Excel file
        FileInputStream fis = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        // Add new data to the sheet
        int rowCount = sheet.getLastRowNum();
        Row row = sheet.createRow(++rowCount);
        row.createCell(0).setCellValue(startTime);
        row.createCell(1).setCellValue(endTime);
        row.createCell(2).setCellValue(totalPass);
        row.createCell(3).setCellValue(totalFailed);
        row.createCell(4).setCellValue(totalTests);
        row.createCell(5).setCellValue(totalSteps);
        row.createCell(6).setCellValue(totalPercentage);

        // Close the input stream
        fis.close();

        // Write the updated Excel file
        FileOutputStream fos = new FileOutputStream(excelFilePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
