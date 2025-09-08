package com.seleniumproject.test;

import org.testng.annotations.Test;

import com.seleniumproject.base.BaseClass;

public class DummyClass2 extends BaseClass{
	
	@Test
	public void dummyTest() {
		String title = driver.getTitle();
		//negative test-case: wrong title: "OrangeHRM1"
		assert title.equals("OrangeHRM1") : "Test Failed - Title is not matching!!!";
		System.out.println("Test Passed - Title is matching");
	}
}
