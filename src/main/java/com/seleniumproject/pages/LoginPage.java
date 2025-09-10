package com.seleniumproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.seleniumproject.actiondriver.ActionDriver;
import com.seleniumproject.base.BaseClass;

public class LoginPage {
	
	private ActionDriver actionDriver;
	
	
	//define locators using By class
	private By usernameInput = By.name("username");
	private By passwordInput = By.cssSelector("input[type='password']");
//	private By loginBtn = By.xpath("//button[contains(text(),'Login')]");
	/***
	 * //button[contains(text(),'Login')]
	 * This may not work reliably because of the <!----> comment nodes inside the button.
	 * The text() function in XPath only considers direct text nodes, so the extra comments may break your match.
	 */
	
	//Option A: Use normalize-space()
	private By loginBtn = By.xpath("//button[normalize-space()='Login']");
	/***
	 * normalize-space() ignores leading/trailing spaces and extra hidden text nodes (like comments).
	 * This is more reliable for buttons with inner comments or extra whitespace.
	 */
	
	//Option B: Use contains() with . instead of text()
//	private By loginBtn = By.xpath("//button[contains(., 'Login')]");
	/***
	 * . selects all text content including child nodes, so comments don’t break it.
	 * Works well if you only need a partial match: e.g., “Login” somewhere inside the button.
	 */
	private By errorMsg = By.xpath("//div[contains(@class,'oxd-alert-content--error')]/p");
	
	
//	//constructor: to initialize ActionDriver object
//	public LoginPage(WebDriver driver) {
//		this.actionDriver = new ActionDriver(driver, BaseClass.getProp());
//	}
	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	
//methods
	/***
	 * Login
	 */
	public void login(String username, String password ) {
		actionDriver.enterText(usernameInput, username);
		actionDriver.enterText(passwordInput, password);
		actionDriver.click(loginBtn);
	}
	
	/***
	 * Validate error message is displayed
	 */
	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMsg);
	}
	
	/***
	 * Get error message text
	 */
	public String getErrorMessageText() {
		return actionDriver.getText(errorMsg);
	}
	
	/***
	 * Validate error message text is displayed correct or not
	 */
	public boolean verifyErrorMessage(String expectedErrorMsg) {
		return actionDriver.compareText(errorMsg, expectedErrorMsg);
	}
}
