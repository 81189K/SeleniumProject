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

public class ActionDriver {
	
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;
	
	public ActionDriver(WebDriver driver, Properties prop) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(prop.getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait)); //TODO: 30 from config.properties
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
			logger.info("clicked on element--->"+elementDescription);
		} catch (Exception e) {
			System.out.println("Unable to click element: "+ e.getMessage());
		}
	}
	
	/***
	 * Method to enter text into an input element.
	 * @modify: avoid code duplication; Fix multiple methods calls
	 */
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
//			driver.findElement(by).clear();
//			driver.findElement(by).sendKeys(value); //TODO: scroll +  click + clear + set + wait
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("entered text: '"+value+"' on element--->"+getElementDescription(by));
		} catch (Exception e) {
			System.out.println("Unable to enter the value in input element: "+ e.getMessage());
		}
	}
	
	/***
	 * Method to get text from an input element.
	 */
	public String getText(By by) {
		String fetchedData = "";
		try {
			waitForElementToBeVisible(by);
			fetchedData = driver.findElement(by).getText(); //TODO: scroll
			//TODO: log message after action.
		} catch (Exception e) {
			System.out.println("Unable to get the text from input element: "+ e.getMessage());
		}
		return fetchedData;
	}
	
	/***
	 * Method to compare two text
	 * @modify: changed return type
	 */
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText(); //TODO: scroll
			if(expectedText.equals(actualText)) {
				System.out.println("Text are matching: "+ actualText + " equals "+ expectedText );
				return true;
			}else {
				System.out.println("Text are not matching: "+ actualText + " not equals "+ expectedText );
				return false;
			}
		} catch (Exception e) {
			System.out.println("Unable to compare texts: "+ e.getMessage());
		}
		return false;
	}
	
	/***
	 * Method to check if an element is displayed or not
	 * @modify: just return the boolean value
	 */
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			System.out.println("Element is not displayed: "+ e.getMessage());
			return false;
		}
	}
	
	/***
	 * scroll to an element
	 */
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			System.out.println("Unable to locate element:" + e.getMessage());
		}
	}
	
	/***
	 * Wait for page to load
	 */
	public void waitForPageLoad(int timeOutInSeconds) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSeconds))
				.until(WebDriver -> ((JavascriptExecutor) WebDriver).executeScript("return document.readyState").equals("complete"));
			System.out.println("Page loaded successfully.");
		} catch (Exception e) {
			System.out.println("Page did not load within " + timeOutInSeconds + " seconds. Exception: " + e.getMessage());
		}
	}
	
	/***
	 * Wait for element to be clickable
	 * @param by
	 */
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			System.out.println("Element is not clickable: "+e.getMessage());
		}
	}
	
	/***
	 * Wait for element to be visible
	 * @param by
	 */
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("Element is not visible: "+e.getMessage());
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
