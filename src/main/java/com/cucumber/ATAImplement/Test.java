package com.cucumber.ATAImplement;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Test {
	
	public static void main(String args[]) {
		WebDriver driver = new FirefoxDriver();
		driver.get("https://www.freepdfconvert.com/pdf-word");
		WebElement e = driver.findElement(By.xpath("//*[text()='Select file']"));
		driver.findElement(By.id("inputFile")).sendKeys("C:/path/to/file.jpg");
		e.click();
		driver.switchTo().activeElement().sendKeys("C:\\Users\\samarth_sikand\\Downloads\\iris-casebase.txt");
		//e.sendKeys("C:\\Users\\samarth_sikand\\Downloads\\iris-casebase.txt");
	}
}
