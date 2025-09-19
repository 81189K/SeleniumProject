package com.seleniumproject.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.seleniumproject.actiondriver.ActionDriver;
import com.seleniumproject.utilities.ExtentReportsManager;
import com.seleniumproject.utilities.LoggerManager;

public class BaseClass {

	// protected because: used within same package and child classes
	private static Properties prop; //protected to private. Since, writing getter method.
//	private static WebDriver driver;	
//	//static not needed for driver because teardown and setup>launchBrowser methods has @before and after method annotations.
//	//nothing wrong; no harm in marking driver as static. For future use.
//	//Marked driver as private from protected. And added getter and setter methods for it.
	
	private static ThreadLocal<WebDriver> driver =new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver =new ThreadLocal<>();
//	private static ActionDriver actionDriver;
	
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	
	/***
	 * load the configuration file
	 * @throws IOException
	 */
	@BeforeSuite	// meaning this will run only once; i.e., prop will be initialized only once. 
	//other tests, starting from 2nd inside classes tag, will fail because they don't have prop value initialized.
	//FIX: since, suite level, make it static, so that all classes will have its value.
	public void loadConfig() throws IOException {
		prop = new Properties();
		//read the file - make object of FileInputStream class
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties"); //throws FileNotFoundException
		//load the file
		prop.load(fis);	//throws IOException
		logger.info("loaded the config.properrties file");
		
		//Start ExtentReports
		ExtentReportsManager.getReporter();
	}
	
	//synchronized: only one thread can use this method at a time.
	@BeforeMethod
	public synchronized void setup() throws IOException {
		//here, lets call the private methods
		System.out.println("Setting up WebDriver for: "+ this.getClass().getName());
		launchBrowser();
		logger.info("WebDriver Initialized");
		configureBrowser();
		logger.info("configurd implict wait and maximized browser window");
		staticWait(2);
		
//		logger.warn("This is a warn message");
//		logger.debug("This is a debug message");
//		logger.trace("This is a trace message");
//		logger.error("This is a error message");
//		logger.fatal("This is a fatal message");
		
//		//Initialize actionDriver object only once
//		if(actionDriver == null) {
//			actionDriver = new ActionDriver(driver, prop);
//			logger.info("ActionDriver instance is created using thread: " + Thread.currentThread().getName());
//		}
		//Initialize actionDriver for the current thread
		actionDriver.set(new ActionDriver(getDriver(), prop));
		logger.info("ActionDriver initialized from thread: " + Thread.currentThread().getName());
	}
	
	/***
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");
		
		if(browser.equalsIgnoreCase("chrome")) {			//use switch or if block.
//			driver = new ChromeDriver();
			driver.set(new ChromeDriver());		//New change as per Thread
			ExtentReportsManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created");
		} else if(browser.equalsIgnoreCase("firefox")) {
//			driver = new FirefoxDriver();
			driver.set(new FirefoxDriver());		//New change as per Thread
			ExtentReportsManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created");
		} else if(browser.equalsIgnoreCase("edge")) {
//			driver = new EdgeDriver();
			driver.set(new EdgeDriver());		//New change as per Thread
			ExtentReportsManager.registerDriver(getDriver());
			logger.info("EdgeDriver Instance is created");
		} else {
			throw new IllegalArgumentException("Browser not supported: "+ browser); //throw exception or make one browser as default.
		}	
	}
	
	/***
	 * Configure Browser settings such as
	 * Implicit Wait
	 * Maximize window
	 * Navigate to URL
	 */
	private void configureBrowser() {
		//Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		
		//Maximize the driver
		getDriver().manage().window().maximize();
		
		//Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the URL: " + e.getMessage());
		}
	}
	
	@AfterMethod
	public synchronized void teardown() {
		if(getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Failed to quit the driver: " + e.getMessage());
			}
		}
//		driver = null;
//		actionDriver = null;
		driver.remove();			//New change as per Thread
		actionDriver.remove();
		ExtentReportsManager.endTest();
		logger.info("WebDriver instance is closed");
	}
	
	/***
	 * Static wait for pause
	 * @param seconds
	 */
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
	

//	/***
//	 * driver WebDriver getter method. Marked driver as private from protected.
//	 * To access it from different package.
//	 */
//	public WebDriver getDriver() {
//		return driver;
//	}
//	
//	/***
//	 * Driver setter method
//	 */
//	public void setDriver(WebDriver driver) {
//		BaseClass.driver = driver;
//	}
	
	//getter method for WebDriver
	public static WebDriver getDriver() {
		if(driver.get()  == null) {
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
	}
	//getter method for ActionDriver
	public static ActionDriver getActionDriver() {
		if(actionDriver.get()  == null) {
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();
	}
	
	/***
	 * prop Properties getter method.
	 * To access it (private) from different package.
	 */
	public static Properties getProp() {
		return prop;
	}
	
}