package com.seleniumproject.test;

import org.testng.annotations.Test;

import com.seleniumproject.base.BaseClass;

public class DummyClass extends BaseClass{
	
	@Test
	public void dummyTest() {
		String title = driver.getTitle();
		/***
		 * assert keyword: is Java’s built-in assertion mechanism.
		 * It’s used to check if a condition is true during runtime.
		 * If the condition is false, the program throws an AssertionError.
		 * 
		 * NOTE: add testng from eclipse marketplace and select trust all.
		 */
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching!!!";
		System.out.println("Test Passed - Title is matching");
	}
}
