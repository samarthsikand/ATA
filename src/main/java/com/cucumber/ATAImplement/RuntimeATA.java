package com.cucumber.ATAImplement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class RuntimeATA {
	
	private static WebDriver driver = new FirefoxDriver();
	private String URL;
	private static List<WebElement> ele = new ArrayList<WebElement>();
	
	public static void main(String args[]) {
		List<String[]> tuples = new ArrayList<String[]>();
		tuples.add(new String[]{"","goto","","http://echoecho.com/htmlforms10.htm"});
		/*tuples.add(new String[]{"textbox","enter","From","Delhi"});
		tuples.add(new String[]{"textbox","enter","To","Jaipur"});
		tuples.add(new String[]{"textbox","click","Date of Journey",""});
		tuples.add(new String[]{"textbox","click","1",""});
		tuples.add(new String[]{"button","click","Search Buses",""});*/
		tuples.add(new String[]{"radiobutton","select","Milk",""});
		runTheTuples(tuples);
	}
	
	private static void runTheTuples(List<String[]> tuples) {
		for(String[] str : tuples) {
			System.out.println(str[0]+" action, "+str[1]+" element, "+str[2]+" data");
			executeTuple(str);
		}
	}
	
	private static void executeTuple(String[] tuple) {
		boolean exactMatched = false;
		if(tuple[1].equalsIgnoreCase("enter")) {
			exactMatched = false;
			ele = driver.findElements(By.xpath("//label[contains(text(),'"+tuple[2]+"')]/following::input[1] | " +
											   "//label[contains(text(),'"+tuple[2]+"')]/descendant::input[1] | " +
											   "//label[contains(text(),'"+tuple[2]+"')]/following::textarea[1] | "+
											   "//label[contains(text(),'"+tuple[2]+"')]/descendant::textarea[1]"));
			System.out.println(ele.isEmpty()+" size: "+ele.size());
			if(!ele.isEmpty() && ele.size() > 1) {
				for(WebElement inputEle : ele) {
					WebElement temp = inputEle.findElement(By.xpath("./preceding::label[1]"));
					if(temp != null && temp.getText().equalsIgnoreCase(tuple[2])) {
						inputEle.sendKeys(tuple[3]);
						exactMatched = true;
						break;
					}
				}
			} else if(!ele.isEmpty() && ele.size() == 1) {
				ele.get(0).sendKeys(tuple[3]);
			} else {
				System.out.println("Element not found");
			}
			
		} else if(tuple[1].equalsIgnoreCase("click") || tuple[1].equalsIgnoreCase("select")) {
			//ele = driver.findElements(By.xpath("//*[text() = 'From']"));
			//System.out.println(ele.isEmpty()+" size: "+ele.size());
			exactMatched = false;
			//ele = driver.findElements(By.xpath("//a[contains(text(),'"+tuple[1]+"')] | //button[contains(text(),'"+tuple[1]+"']"));
			if(tuple[0].equalsIgnoreCase("button")) {
				ele = driver.findElements(By.xpath("//button[contains(text(),'"+tuple[2]+"')]"));
				if(!ele.isEmpty()) {
					ele.get(0).click();
				} else {
					System.out.println("The button "+ tuple[2] + " could not be found.");
				}
					
			} else if(tuple[0].equalsIgnoreCase("link")) {
				ele = driver.findElements(By.xpath("//a[contains(text(),'"+tuple[2]+"')]"));
				exactMatched = true;
				ele.get(0).click();
				if(!ele.isEmpty()) {
					ele.get(0).click();
				} else {
					System.out.println("The link "+ tuple[2] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("textbox")) {
				ele = driver.findElements(By.xpath("//label[contains(text(),'"+tuple[2]+"')]/following::input[1] | " +
						   						   "//label[contains(text(),'"+tuple[2]+"')]/descendant::input[1]"));
				exactMatched = true;
				if(!ele.isEmpty()) {
					ele.get(0).click();
				} else {
					System.out.println("The textbox "+ tuple[2] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("radiobutton")) {
				ele = driver.findElements(By.xpath("//input[@type='radio' and contains(@value,'"+tuple[2]+"')]"));
				//ele = driver.findElements(By.xpath("//*[contains(text(),'Milk')]/preceding::input[@type='radio']"));
				if(!ele.isEmpty()) {
					for(WebElement checkEle : ele) {
						System.out.println("radio " + checkEle.getAttribute("name") + " value : " + checkEle.getAttribute("value"));
					}
					System.out.println("radio " + ele.get(0).getText());
					//ele.get(0).click();
				} else {
					System.out.println("The radio button "+ tuple[2] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("image")) {
				System.out.println("Sorry cannot interact with images");
			} else {
				System.out.println("Sorry Cannot interact with" + tuple[0] + " element");
			}
			
		} else if(tuple[1].equalsIgnoreCase("goto")) {
			System.out.println("Naviagting...");
			driver.manage().window().maximize();
			driver.navigate().to(tuple[3]);
			
		} else if(tuple[1].equalsIgnoreCase("exists")){
			ele = driver.findElements(By.xpath(""));
		}
	}
}
