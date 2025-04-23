package testCases;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

public class TC001_LoginTest extends BaseTest{
	
	@Test
	public void verify_login() {
		try {
			//login to merchant profile
			login();
			
			Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='menu']/following-sibling::div[1]/div[1]"))).isDisplayed(), "Login Unsuccessful");			
			
		} catch (Exception e) {
			Assert.fail();
		}
	}

}
