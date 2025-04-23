package testCases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

public class TC002_NavigationTest extends BaseTest {
	
		@Test
		public void navigation_test() {
			
			try {
				//login to the merchant profile
				login();
				
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='menu']"))).click(); //Click menu button
				
				//List of menu items
				String[] menuItems= {"Dashboard","Profile","Upgrade Plan","Order History","Manage Specification","All Enquiry","My Lead"," Get Application","MediaLibrary","Staff Details","Faq","Reset Password","Visiting Cards","Report"};
				
				//List of menuItems URL endpoints
				String[] endpoints= {"dashboard","businessprofile","upgrade","ordertable","managespecification","enquiry","leads","get-application","medialibrary","staffdetails","faq","resetpassword","visitingfrom","merchantreport"};
				
							
				// Loop through each menu item, click, and verify navigation
				for(int i=0;i<menuItems.length;i++) {
					String itemXpath="//span[text()='"+ menuItems[i]+"']";
					
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath(itemXpath))).click();
					
					// Wait until page is fully loaded
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(itemXpath)));
			       
					String actualURL=driver.getCurrentUrl();
					String expectedURL="https://www.business.aroundme.co.in/merchant/"+endpoints[i];
					
					softassert.assertEquals(actualURL, expectedURL);
					
	                // Go back to menubar
					if(i!=menuItems.length-1) {
						wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='menu']"))).click(); //Click menu button
						
						//driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
					}
					
				}
				
							
			} catch (Exception e) {
				Assert.fail();
			}
			softassert.assertAll();
		}
}
