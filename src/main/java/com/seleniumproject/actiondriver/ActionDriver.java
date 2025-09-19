package com.seleniumproject.actiondriver;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.seleniumproject.base.BaseClass;
import com.seleniumproject.utilities.ExtentReportsManager;

public class ActionDriver {
	
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;
	
	public ActionDriver(WebDriver driver, Properties prop) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(prop.getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait)); //30 seconds from config.properties
		logger.info("WebDriver instance is created");
	}
	
	/***
	 * Method to click an element.
	 */
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by); 
			//note: if an element is clickable, it’s definitely visible. But the reverse isn’t true — a visible element might still be disabled or blocked
			driver.findElement(by).click(); //TODO: scrollIntoView before click.
			ExtentReportsManager.logStep("clicked on "+ elementDescription);
			logger.info("clicked on element--->"+elementDescription);
		} catch (Exception e) {
//			System.out.println("Unable to click element: "+ e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Unable to click on "+ elementDescription, "Unable to click on "+ elementDescription);
			logger.error("Unable to click on "+ elementDescription);
		}
	}
	
	/***
	 * Method to enter text into an input element.
	 * @modify: avoid code duplication; Fix multiple methods calls
	 */
	public void enterText(By by, String value) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeVisible(by);
//			driver.findElement(by).clear();
//			driver.findElement(by).sendKeys(value); //TODO: scroll +  click + clear + set + wait
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			ExtentReportsManager.logStep("Entered text '"+ value +"' in "+ elementDescription);
			logger.info("Entered text '"+ value +"' in "+ elementDescription);
		} catch (Exception e) {
//			System.out.println("Unable to enter the value in input element: "+ e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Unable to enter text in "+ elementDescription, "Unable to enter text in "+ elementDescription);
			logger.error("Unable to enter text in "+ elementDescription +" - "+ e.getMessage());
		}
	}
	
	/***
	 * Method to get text from an input element.
	 */
	public String getText(By by) {
		String fetchedData = "";
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeVisible(by);
			fetchedData = driver.findElement(by).getText(); //TODO: scroll
			ExtentReportsManager.logStep("Fetched text '"+ fetchedData +"' from "+ elementDescription);
			logger.info("Fetched text '"+ fetchedData +"' from "+ elementDescription);
		} catch (Exception e) {
//			System.out.println("Unable to get the text from input element: "+ e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Unable to enter text in "+ elementDescription, "Unable to enter text in "+ elementDescription);
			logger.error("Unable to get the text from input element: "+ e.getMessage());
		}
		return fetchedData;
	}
	
	/***
	 * Method to compare two text
	 * @modify: changed return type
	 */
	public boolean compareText(By by, String expectedText) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText(); //TODO: scroll
			ExtentReportsManager.logStep("Fetched text '"+ actualText +"' from "+ elementDescription);
			logger.info("Fetched text '"+ actualText +"' from "+ elementDescription);
			if(expectedText.equals(actualText)) {
//				System.out.println("Text are matching: "+ actualText + " equals "+ expectedText );
				ExtentReportsManager.logStepWithScreenshot(BaseClass.getDriver(), "Actual text '"+ actualText +"' matching with Expected text '"+ expectedText + "'", "Actual text '"+ actualText +"' matching with Expected text '"+ expectedText + "'");
				logger.info("Actual text '"+ actualText +"' matching with Expected text '"+ expectedText + "'");
				return true;
			}else {
//				System.out.println("Text are not matching: "+ actualText + " not equals "+ expectedText );
				ExtentReportsManager.logFailure(BaseClass.getDriver(), "Actual text '"+ actualText +"' is NOT matching with Expected text '"+ expectedText + "'", "Actual text '"+ actualText +"' is NOT matching with Expected text '"+ expectedText + "'");
				logger.error("Actual text '"+ actualText +"' is NOT matching with Expected text '"+ expectedText + "'");
				return false;
			}
		} catch (Exception e) {
//			System.out.println("Unable to compare texts: "+ e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Unable to compare texts: "+ e.getMessage(), "Unable to compare texts: "+ e.getMessage());
			logger.error("Unable to compare texts: "+ e.getMessage());
					
		}
		return false;
	}
	
	/***
	 * Method to check if an element is displayed or not
	 * @modify: just return the boolean value
	 */
	public boolean isDisplayed(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeVisible(by);
			ExtentReportsManager.logStep(elementDescription +" is visible");
			logger.info(elementDescription +" is visible");
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
//			System.out.println("Element is not displayed: "+ e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Element is not displayed: "+ e.getMessage(), "Element is not displayed: "+ e.getMessage());
			logger.error("Element is not displayed: "+ e.getMessage());
			return false;
		}
	}
	
	/***
	 * scroll to an element
	 */
	public void scrollToElement(By by) {
		String elementDescription = getElementDescription(by);
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			ExtentReportsManager.logStep("Scolled to Element '"+ elementDescription +"'");
			logger.info("Scolled to Element '"+ elementDescription +"'");
		} catch (Exception e) {
//			System.out.println("Unable to locate element:" + e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Failed to scoll to Element '"+ elementDescription +"': "+ e.getMessage(), "Failed to scoll to Element '"+ elementDescription +"': "+ e.getMessage());
			logger.error("Failed to scoll to Element '"+ elementDescription +"': "+ e.getMessage());
		}
	}
	
	/***
	 * Wait for page to load
	 */
	public void waitForPageLoad(int timeOutInSeconds) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSeconds))
				.until(WebDriver -> ((JavascriptExecutor) WebDriver).executeScript("return document.readyState").equals("complete"));
//			System.out.println("Page loaded successfully.");
			ExtentReportsManager.logStep("Page loaded successfully.");
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
//			System.out.println("Page did not load within " + timeOutInSeconds + " seconds. Exception: " + e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Page did not load within " + timeOutInSeconds + " seconds. Exception: " + e.getMessage(), "Page did not load within " + timeOutInSeconds + " seconds. Exception: " + e.getMessage());
			logger.error("Page did not load within " + timeOutInSeconds + " seconds. Exception: " + e.getMessage());
		}
	}
	
	/***
	 * Wait for element to be clickable
	 * @param by
	 */
	private void waitForElementToBeClickable(By by) {
		String elementDescription = getElementDescription(by);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
			ExtentReportsManager.logStep("Element '"+ elementDescription +"' is clickable");
			logger.info("Element '"+ elementDescription +"' is clickable");
		} catch (Exception e) {
//			System.out.println("Element is not clickable: "+e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Element "+elementDescription+" is not clickable: "+e.getMessage(), "Element "+elementDescription+" is not clickable: "+e.getMessage());
			logger.error("Element "+elementDescription+" is not clickable: "+e.getMessage());
		}
	}
	
	/***
	 * Wait for element to be visible
	 * @param by
	 */
	private void waitForElementToBeVisible(By by) {
		String elementDescription = getElementDescription(by);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			ExtentReportsManager.logStep("Element "+elementDescription+" is visible");
			logger.info("Element "+elementDescription+" is visible");
		} catch (Exception e) {
//			System.out.println("Element is not visible: "+e.getMessage());
			ExtentReportsManager.logFailure(BaseClass.getDriver(), "Element "+elementDescription+" is not visible: "+e.getMessage(), "Element "+elementDescription+" is not visible: "+e.getMessage());
			logger.error("Element "+elementDescription+" is not visible: "+e.getMessage());
		}
	}
	
	/***
	 * get element description using By locator.
	 */
	public String getElementDescription(By locator) {
	    if (driver == null) {
	        logger.error("Driver is null, cannot find element for locator: {}", locator);
	        return "Unknown element (driver is null)";
	    }
	    if (locator == null) {
	        logger.error("Locator is null");
	        return "Unknown element (locator is null)";
	    }

	    try {
	        WebElement element = driver.findElement(locator);

	        // candidate attributes in priority order
	        Map<String, String> attributes = new LinkedHashMap<>();
	        attributes.put("id", element.getDomAttribute("id"));
	        attributes.put("name", element.getDomAttribute("name"));
	        attributes.put("text", element.getText());
	        attributes.put("class", element.getDomAttribute("class"));
	        attributes.put("placeholder", element.getDomAttribute("placeholder"));

	        for (Map.Entry<String, String> entry : attributes.entrySet()) {
	            if (isNotEmpty(entry.getValue())) {
	                return "Element with " + entry.getKey() + ": " + truncate(entry.getValue(), 30);
	            }
	        }

	    } catch (Exception e) {
	        logger.error("Unable to describe element for locator {}: {}", locator, e.getMessage(), e);
	    }

	    return "Unknown element";
	}


	/***
	 * Utility Method: to check String is not null or empty
	 */
	private boolean isNotEmpty(String value) {
		return value != null && !value.isBlank();
	}

	/***
	 * Utility Method: to check String is not null or empty
	 */
	private String truncate(String value, int maxLength) {
		return (value == null || value.length() <= maxLength) ? value : value.substring(0, maxLength);
	}

	
}
