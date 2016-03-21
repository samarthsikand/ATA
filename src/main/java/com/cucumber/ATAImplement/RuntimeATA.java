package com.cucumber.ATAImplement;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class RuntimeATA {
	
	private WebDriver driver = new FirefoxDriver();
	private String URL;
	private List<WebElement> ele = new ArrayList<WebElement>();
	
	
	public void runTheTuples(List<String[]> tuples) {
		for(String[] str : tuples) {
			System.out.println(str[0]+" action, "+str[1]+" element, "+str[2]+" data");
			executeTuple(str);
		}
	}
	
	public void executeTuple(String[] tuple) {
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
				ele = driver.findElements(By.xpath("//input[@type='radio' and @value='"+tuple[2]+"']"));
				//ele = driver.findElements(By.xpath("//*[contains(text(),'Milk')]/preceding::input[@type='radio']"));
				if(!ele.isEmpty()) {
					ele.get(0).click();
				} else {
					System.out.println("The radio button "+ tuple[2] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("radiobutton group")) {
				String[] listRadio = tuple[2].split("/");
				ele = driver.findElements(By.xpath("//*[text()='"+listRadio[0]+"']/following::input[@type='radio' and @value='"+listRadio[1]+"']"));
				if(!ele.isEmpty()) {
					ele.get(0).click();
				} else {
					System.out.println("The radio button group "+ listRadio[0] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("checkbox")) {
				ele = driver.findElements(By.xpath("//label[text()='"+tuple[1]+"']/preceding-sibling::input[@type='checkbox'] | //input[@type='checkbox' and @value='"+tuple[1]+"'"));
				if(!ele.isEmpty()) {
					ele.get(0).click();
				} else {
					System.out.println("The checkbox "+ tuple[1] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("multiselect")) {
				Select se = null;
				String[] prefixSelect = tuple[2].split("/");
				String[] listSelect = null;
				if(prefixSelect.length > 1) {
					listSelect = prefixSelect[1].split(",");
				} else {
					listSelect = prefixSelect[0].split(",");
				}
				
				if(prefixSelect.length != 1) {
					ele = driver.findElements(By.xpath("//*[text()='"+prefixSelect[0]+"']/following::select[@multiple]"));
				} else {
					ele = driver.findElements(By.xpath("//select[@multiple]"));
				}
				
				if(!ele.isEmpty()) {
					for(WebElement multiEle : ele) {
						 se = new Select(multiEle);
						 if(se.isMultiple()) {
							 for(String str : listSelect) {
								 se.selectByVisibleText(str);
							 }
							 break;
						 }
					}
				} else {
					System.out.println("The multiselct "+ tuple[1] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("dropdown")) {
				Select se = null;
				String[] prefixSelect = tuple[2].split("/");
				if(prefixSelect.length != 1) {
					ele = driver.findElements(By.xpath("//*[text()='"+prefixSelect[0]+"']/following::select[1]"));
				} else {
					ele = driver.findElements(By.xpath("//select[1]"));
				}
				
				if(!ele.isEmpty()) {
					se = new Select(ele.get(0));
					if(prefixSelect.length != 1) {
						se.selectByVisibleText(prefixSelect[1]);
					} else {
						se.selectByVisibleText(prefixSelect[0]);
					}
				} else {
					System.out.println("The select "+ prefixSelect[0] + " could not be found.");
				}
				
			} else if(tuple[0].equalsIgnoreCase("alert")) {
				Alert simpleAlert = driver.switchTo().alert();
				if(tuple[2].equalsIgnoreCase("ok") || tuple[2].equalsIgnoreCase("accept")) {
					simpleAlert.accept();
				} else {
					simpleAlert.dismiss();
				}	
				
			} else if(tuple[0].equalsIgnoreCase("popup")) {
				String mainWindowHandle = driver.getWindowHandle();
				for (String activeHandle : driver.getWindowHandles()) {
			        if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			        }
			    }
				
			} else if(tuple[0].equalsIgnoreCase("file select")) {
				ele = driver.findElements(By.xpath("//input[@type='file']"));
				WebElement element = driver.findElement(By.xpath("//input[@type='file']"));
				element.sendKeys("C:/Users/samarth_sikand/Downloads/iris-casebase.txt");
//				new Actions(driver).click(element).perform();
//				driver.switchTo().activeElement().sendKeys("test.html");
				if(!ele.isEmpty()) {
					/*for(WebElement fileEle : ele) {
						if(fileEle.getAttribute("id").equals("clientUpload")) {
							System.out.println("Found it.."+fileEle.getText());
							fileEle.click();
							driver.switchTo().activeElement().sendKeys(tuple[2]);
							break;
						}
					}*/
					//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					//ele.get(0).sendKeys(tuple[2]);
				} else {
					System.out.println("The file select "+ tuple[0] + " could not be found.");
				}
			} else if(tuple[0].equalsIgnoreCase("calendar")) {
				String[] date = tuple[3].split(",");
				System.out.println("Month: "+date[0]+" Day:"+date[1]);
				ele = driver.findElements(By.xpath("//table/descendant::*[text()='"+date[0]+"']"));
				System.out.println("The tables size: "+ele.size());
				int visible = 0;
				for(WebElement temp : ele) {
					if(temp.isDisplayed()) {
						break;
					}
					visible++;
				}
				WebElement element = ele.get(visible).findElement(By.xpath("./ancestor::table/descendant::td[text()='"+date[1]+"']"));
				element.click();
				
			} else if(tuple[0].equalsIgnoreCase("image")) {
				System.out.println("Sorry cannot interact with images");
			} else {
				System.out.println("Sorry Cannot interact with " + tuple[0] + " element");
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
