package testCases;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

public class TC003_SearchFieldTest extends BaseTest  {
	@Test
	public void searchfield_test() {
		try {
			//login to merchant profile
			login();
			//Thread.sleep(1000);
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='menu']"))).click(); //Click menu button
			//Thread.sleep(1000);
			
			//click on "My Lead" option in menu bar
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='My Lead']"))).click();
			//Thread.sleep(1000);
			
			List<WebElement> lst=driver.findElements(By.xpath("//div[@class='MuiDataGrid-cellContent']"));
			
			//generate random number between 0 and list size
			Random rand=new Random();
			int randomNum=rand.nextInt(lst.size())+1;
			
			//get name from random row
			String name=wait.until(ExpectedConditions.elementToBeClickable(lst.get(randomNum))).getText();
			
			//copy paste above name into search field
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']"))).sendKeys(name);
			//Thread.sleep(2000);
			
			//get search result
			String result=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@role='presentation'])[18]"))).getText();
			
			//compare search result with search field input
			Assert.assertEquals(result, name);
			
			
		} catch (Exception e) {
			Assert.fail();
		}
	}

}
