package com.seleniumproject.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seleniumproject.base.BaseClass;
import com.seleniumproject.pages.HomePage;
import com.seleniumproject.pages.LoginPage;
import com.seleniumproject.utilities.ExtentReportsManager;

public class LoginPageTest extends BaseClass{
	
	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test
	public void verifyValidLoginTest() {
		ExtentReportsManager.startTest("Verify Login Test");
		System.out.println("Running testmethod on thread: " + Thread.currentThread().getName());
		loginPage.login("admin", "admin123");
		Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab is NOT visible after login");
		ExtentReportsManager.logStepWithScreenshot(getDriver(), "Admin tab is visible after login; Successfully verified Login Test", "Admin tab is visible after login; Successfully verified Login Test");
		homePage.logout();
		ExtentReportsManager.logStep("Logged out successfully");
		staticWait(1);
	}
	
	@Test
	public void verifyInvalidLoginTest() {
		ExtentReportsManager.startTest("Verify Invalid Login Test");
		System.out.println("Running testmethod on thread: " + Thread.currentThread().getName());
		loginPage.login("admin", "admin123A");
		String expectedErrMsg = "Invalid credentials1";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrMsg), "Test Failed: incorrect error message");
		ExtentReportsManager.logStepWithScreenshot(getDriver(), "Successfully verified Invalid Login Test", "Successfully verified Invalid Login Test");
	}

}
