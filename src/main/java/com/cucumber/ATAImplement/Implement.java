package com.cucumber.ATAImplement;

import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import org.openqa.selenium.By; 
import org.openqa.selenium.JavascriptExecutor; 
import org.openqa.selenium.WebDriver; 
import org.openqa.selenium.WebElement; 
import org.openqa.selenium.firefox.FirefoxDriver;

public class Implement {

	private static WebDriver driver = new FirefoxDriver();
	private String URL;
	private static WebElement ele = null;

	public static void main(String args[]) {
	 navigateToWebsite();
	 insertIntoFieldValues("From","Delhi");
	 insertIntoFieldValues("To","Jaipur");
	 insertIntoFieldValues("Date of Journey","01-Mar-2016");
	 insertIntoFieldValues("Date of Return","03-Mar-2016");
	 clickButton("Search Buses");
	}
	 public static void navigateToWebsite() {
		 driver.manage().window().maximize();
		 driver.navigate().to("http://redbus.in");
	 }

	 public static void insertIntoFieldValues(String field,String value) {
		 ele = driver.findElement(By.xpath("//label[contains(text(),'"+field+"')]/following::input[1]"));
		 if(ele.getAttribute("readonly") != null && ele.getAttribute("readonly").equals("true")) {
			 ((JavascriptExecutor)driver).executeScript("document.getElementById('"+ele.getAttribute("id")+"').removeAttribute('readonly');");
			 ele.clear();
			 ele.sendKeys(value);
		 } else {
			 ele.sendKeys(value);
		}
	}

	 public static void clickButton(String button) {
		 String newXpath = "//*[contains(text(),'"+button+"')]";
		 List<WebElement> listEle = driver.findElements(By.xpath(newXpath));
		 for(WebElement ele : listEle) {
			 if(ele.getAttribute("class").contains("button")) {
				 ele.click();
				 break;
			 }
		}
	}

}