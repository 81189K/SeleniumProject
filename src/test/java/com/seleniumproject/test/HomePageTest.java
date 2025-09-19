package com.seleniumproject.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seleniumproject.base.BaseClass;
import com.seleniumproject.pages.HomePage;
import com.seleniumproject.pages.LoginPage;
import com.seleniumproject.utilities.ExtentReportsManager;

public class HomePageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	
	@Test
	public void verifyOrangeHRMLogo() {
		ExtentReportsManager.startTest("Verify Login Test");
		System.out.println("Running testmethod on thread: " + Thread.currentThread().getName());
		loginPage.login("admin", "admin123");
		Assert.assertTrue(homePage.isLogoVisible(), "Test Failed: Logo is not visisble");
		ExtentReportsManager.logStepWithScreenshot(getDriver(), "Logo is visible in home page", "Logo is visible in home page");
	}

}
