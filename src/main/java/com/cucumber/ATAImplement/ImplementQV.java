package com.cucumber.ATAImplement;

import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.ArrayList;
import org.openqa.selenium.By; 
import org.openqa.selenium.JavascriptExecutor; 
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.WebElement; 
import org.openqa.selenium.firefox.FirefoxDriver;
import com.cucumber.ATAImplement.Tree.Node;
import com.google.common.collect.Lists;

public class ImplementQV {

	private static WebDriver driver = new FirefoxDriver();
	private String URL;
	private static WebElement ele = null;

		private static MainQV qvObj = new MainQV(driver);

	public static void main(String args[]) {
	List<WebElement> listOfElement = driver.findElements(By.xpath("//html"));

	Node<WebElement> rootNode = null;
	List<String> labels = new ArrayList<String>();
		 navigateToWebsite();
		 labels = Lists.newArrayList("SMALL BUSINESS","PERSONAL");
		 clickButton(labels);
		 labels = Lists.newArrayList("Learn more","Business TV");
		 clickButton(labels);
	}
	 public static void navigateToWebsite() {
		 driver.manage().window().maximize();
		 driver.navigate().to("http://www.bell.ca");
	 }

	 public static void clickButton(List<String> labels) {
		 ele = qvObj.returnWebElement(labels);
		 ele.click();
	}

}