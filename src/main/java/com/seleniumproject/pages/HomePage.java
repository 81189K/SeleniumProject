package com.seleniumproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.seleniumproject.actiondriver.ActionDriver;
import com.seleniumproject.base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;
	
	
	//define locators using By class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDBtn = By.className("oxd-userdropdown-name");
	private By logoutBtn = By.xpath("//a[text()='Logout']");
	private By logo = By.cssSelector(".oxd-brand-banner img");
	

//	//constructor: to initialize ActionDriver object
//	public HomePage(WebDriver driver) {
//		this.actionDriver = new ActionDriver(driver, BaseClass.getProp());
//	}
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	
	//methods
	/***
	 * verify if admin tab is visible
	 */
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	
	/***
	 * verify if logo is visible
	 */
	public boolean isLogoVisible() {
		return actionDriver.isDisplayed(logo);
	}
	
	/***
	 * logout
	 */
	public void logout() {
		actionDriver.click(userIDBtn);
		actionDriver.click(logoutBtn);
	}
}
