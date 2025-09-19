package com.seleniumproject.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportsManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>(); // it defines the tests to be written in the
																		// report
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize the ExtentReports
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			// create object of ExtentSparkReporter
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);

			// initialize
			extent = new ExtentReports();
			//link spart with extent
			extent.attachReporter(spark);

			// Adding system info
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

	// start the Test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// End a Test
	public synchronized static void endTest() {
		getReporter().flush(); // Writes test information from the started reporters to their output view
	}

	// get current Thread's test
	public synchronized static ExtentTest getTest() {
		return test.get(); // Returns the value in the current thread's copy of thisthread-local variable
	}

	// method to get the name of the current test
	public static String getTestName() {
		ExtentTest currenTest = getTest();
		if (currenTest != null) {
			return currenTest.getModel().getName();
		} else {
			return "No test is currently active for this thread";
		}
	}

	// log a step validation with Screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().pass(logMessage);
		// Screenshot method
		attachScreenshot(driver, screenshotMessage);
	}

	// log a step failure with Screenshot
	public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage) {
		String failureMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(failureMessage);
		// Screenshot method
		attachScreenshot(driver, screenshotMessage);
	}

	// log a skip
	public static void logSkip(WebDriver driver, String logMessage, String screenshotMessage) {
		String skipMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(skipMessage);
	}

	// Take screenshot with data and time in the file
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);

		// data and time format for file name
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		// Set screenshotDestinationPath
		String screenshotDestinationPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
				+ screenshotName + "_" + timeStamp + ".png";
		File screenshotFilePath = new File(screenshotDestinationPath);

		// Save the screenshot
		try {
			FileUtils.copyFile(src, screenshotFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Convert screenshot to Base64
		String base64Format = convertToBase64(src);
		return base64Format;
	}

	// Convert screenshot to Base64 format
	public static String convertToBase64(File screenshotFile) {
		String base64Format = "";
		try {
			// Read the file content into a byte array
			byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
			// Convert the byte array to Base64 String
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Format;
	}
	
	//Add screenshot to report using Base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenShotBase64 = takeScreenshot(driver, getTestName());
			//to attach screenshot to report
			getTest().info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach screenshot: "+message);
			e.printStackTrace();
		}
	}

	// log a step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	// Register WebDriver for current thread
	@SuppressWarnings("deprecation")
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}

}
