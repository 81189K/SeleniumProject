package com.seleniumproject.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.seleniumproject.base.BaseClass;
import com.seleniumproject.utilities.ExtentReportsManager;

public class DummyClass2 extends BaseClass{
	
	@Test
	public void dummyTest() {
		ExtentReportsManager.startTest("Dummy Test2");
		String title = getDriver().getTitle();
		//negative test-case: wrong title: "OrangeHRM1"
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching!!!";
//		System.out.println("Test Passed - Title is matching");
		ExtentReportsManager.logSkip(getDriver(), "Skipping the test as part of testing", "Skipping the test as part of testing");
		throw new SkipException("Skipping the test as part of testing");
	}
}
