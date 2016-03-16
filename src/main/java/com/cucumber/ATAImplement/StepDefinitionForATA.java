package com.cucumber.ATAImplement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.w3c.dom.Node;

import static org.joox.JOOX.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitionForATA {
	
	private WebDriver driver = new FirefoxDriver();
	private String URL;
	private Map<String,WebElement> mapLabelAndElement = new HashMap<String,WebElement>();
	
	@Given("^I am on \"([^\"]*)\" website$")
	public void i_am_on_website(String arg1) throws Throwable {
	    driver.navigate().to("http://"+arg1);
	    URL = driver.getCurrentUrl();
	    /*WebElement ele = driver.findElement(By.xpath("//label[contains(.,'Date of Return')]"));
	    if(ele != null)
	    	System.out.println("Name goes here :" +ele.toString());*/
	    createMapOfLabelAndInput();
	    System.out.println(mapLabelAndElement.keySet());
	}

	@When("^I set \"([^\"]*)\" field to \"([^\"]*)\"$")
	public void i_set_field_to(String arg1, String arg2) throws Throwable {
	    if(mapLabelAndElement.containsKey(arg1) && mapLabelAndElement.get(arg1) != null) {
	    	//System.out.println(arg1+": "+mapLabelAndElement.get(arg1).getAttribute("readonly"));
	    	if(mapLabelAndElement.get(arg1).getAttribute("readonly") != null && mapLabelAndElement.get(arg1).getAttribute("readonly").equals("true")) {
	    		System.out.println("Has it come here");
	    		((JavascriptExecutor)driver).executeScript("document.getElementById('"+mapLabelAndElement.get(arg1).getAttribute("id")+"').removeAttribute('readonly');");
	    		mapLabelAndElement.get(arg1).clear();
	    		mapLabelAndElement.get(arg1).sendKeys("01-Mar-2016");
	    	} else {
	    		mapLabelAndElement.get(arg1).sendKeys(arg2);
	    	}
	    	
	    }
	}

	@When("^I click \"([^\"]*)\" button$")
	public void i_click_button(String arg1) throws Throwable {
		String xpath = "//button[contains(.,'"+arg1+"')] | //input[@type='submit']/following::span[contains(.,'"+arg1+"')]";
		String newXpath = "//*[contains(text(),'"+arg1+"')]";
		List<WebElement> listEle = driver.findElements(By.xpath(newXpath));
		for(WebElement ele : listEle) {
			System.out.println("Element detected: "+ele.getText());
			if(ele.isDisplayed() && (ele.getAttribute("class").contains("button"))) {
				clickElement(ele);
				break;
			}
		}
		//WebElement buttonEle = driver.findElement(By.xpath("//input[@type='submit']/following::span[contains(.,'Login')]"));
		//ele.click();
	}
	
	@When("^I click on \"([^\"]*)\" link$")
	public void i_click_on_link(String arg1) throws Throwable {
		String xpath = "//a[contains(.,'"+arg1+"')]";
	    List<WebElement> ele = driver.findElements(By.xpath(xpath));
		//WebElement ele = driver.findElement(By.id("nav-link-yourAccount"));
	    for(WebElement iterEle : ele) {
	    	if(iterEle.isDisplayed()) {
	    		clickElement(iterEle);
	    		//iterEle.click();
	    		break;
	    	}
	    }
	}
	
	@Then("^I verify the results$")
	public void i_verify_the_results() throws Throwable {
	    
	}
	
	public void createMapOfLabelAndInput() {
		List<WebElement> listOfInputs = driver.findElements(By.tagName("label"));
		List<WebElement> listOfparents;
		String path = "";
		//Iterator<WebElement> iterWebElement = new Iterator<WebElement>(listOfInputs);
		WebElement inputEle = null;
	    for(WebElement ele : listOfInputs) {
	    	path = generateXPATH(ele, "");
	    	System.out.println("Path of \""+ele.getText()+ "\" is: "+path);
	    	if(!mapLabelAndElement.containsKey(ele.getText())) {
	    		try {
	    			inputEle = driver.findElement(By.xpath("//label[contains(text(),'"+ ele.getText() +"')]/following::input[1]"));
	    		} catch(Exception e) {
	    			inputEle = null;
	    			System.out.println("Element not found");
	    		}
	    		
	    		mapLabelAndElement.put(ele.getText(), inputEle);
	    	}
	    }
	}
	
	public void clickElement(WebElement ele) {
		ele.click();
		if(!driver.getCurrentUrl().equalsIgnoreCase(URL)) {
			createMapOfLabelAndInput();
		}
	}
	
	private String generateXPATH(WebElement childElement, String current) {
	    String childTag = childElement.getTagName();
	    if(childTag.equals("html")) {
	        return "/html[1]"+current;
	    }
	    WebElement parentElement = childElement.findElement(By.xpath("..")); 
	    List<WebElement> childrenElements = parentElement.findElements(By.xpath("*"));
	    int count = 0;
	    for(int i=0;i<childrenElements.size(); i++) {
	        WebElement childrenElement = childrenElements.get(i);
	        String childrenElementTag = childrenElement.getTagName();
	        if(childTag.equals(childrenElementTag)) {
	            count++;
	        }
	        if(childElement.equals(childrenElement)) {
	            return generateXPATH(parentElement, "/" + childTag + "[" + count + "]"+current);
	        }
	    }
	    return null;
	}
}
