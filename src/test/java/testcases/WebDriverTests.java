package testcases;

import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;
import common.configurations.BaseTest;
import common.configurations.WebDriverManager;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;
import webdriver.helper.MobileElement;
import webdriver.helper.PageElement;
import webdriver.helper.WebPage;

/**
 * @author sabarinath.s
 * Date: 20-Apr-2016	
 * Time: 11:53:31 am 
 */

public class WebDriverTests extends BaseTest{

	
	@Test(groups={"test"})
	public void testChromeDriver(){
		WebDriverManager.getWebDriver(true);
	}
	
	public static void main(String[] a){
		
		testPageDecorator();
	}
	
	@Test
	public static void testPageDecorator(){
		
		SamplePage page = new SamplePage();
		
		System.out.println(page.browserElement.getBy());
		System.out.println(page.androidElement.getBy());
		System.out.println(page.browserElement1.getBy());
		System.out.println(page.androidElement1.getBy());

		
	}
	
	static class SampleBrowserPage extends WebPage{
		
		
		@FindBy(id="browser")
		public PageElement browserElement;
		
		@AndroidFindBy(id="Android")
		public MobileElement androidElement;
		
		@iOSFindBy(id="iOS")
		public MobileElement iOSElement;
		
	}
	
	static  class SamplePage extends SampleBrowserPage{
		 
			
			@FindBy(id="browser1")
			public PageElement browserElement1;
			
			@AndroidFindBy(id="Android1")
			public PageElement androidElement1;
			
			@iOSFindBy(id="iOS1")
			public PageElement iOSElement1;
	 }
}
