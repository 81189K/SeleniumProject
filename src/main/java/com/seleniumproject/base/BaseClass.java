package com.seleniumproject.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseClass {

	protected Properties prop; // used within same package and child classes
	protected WebDriver driver;

	@BeforeMethod
	public void setup() throws IOException {
		//load the configuration file
		prop = new Properties();
		//read the file - make object of FileInputStream class
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties"); //throws FileNotFoundException
		//load the file
		prop.load(fis);	//throws IOException
		
		
		//Initialize the WebDriver based on browser defined in config.properties file
		String browser = prop.getProperty("browser");
		
		if(browser.equalsIgnoreCase("chrome")) {			//use switch or if block.
			driver = new ChromeDriver();
		} else if(browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if(browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else {
			throw new IllegalArgumentException("Browser not supported: "+ browser); //throw exception or make one browser as default.
		}
		
		//Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		
		//Maximize the driver
		driver.manage().window().maximize();
		
		//Navigate to URL
		driver.get(prop.getProperty("url"));
	}
	
	@AfterMethod
	public void teardown() {
		if(driver != null) {
			driver.quit();
		}
	}
}