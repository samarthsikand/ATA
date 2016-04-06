package com.cucumber.ATAImplement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.cucumber.ATAImplement.AutomationATA.Tuple;

public class AutomationATAMain {
	public static void main(String args[]) {
		String strLine;
		AutomationATA newObj = new AutomationATA();
		newObj.driver = new FirefoxDriver();
		File file = new File("C:/Users/samarth_sikand/Desktop/nationwide.html");
		try {
			Document doc = Jsoup.parse(file,"UTF-8");
			List<Tuple> listOfTuples = new ArrayList<Tuple>();
			
//			driver.navigate().to("http://bell.ca");
//			driver.findElement(By.xpath("//button[text()='Save my selections']")).click();
			 
			newObj.driver.navigate().to("http://nationwide.co.uk");
//			driver.findElement(By.xpath("//a[@class='iconLink close']")).click();
//			driver.findElements(By.xpath("//a[contains(text(),'ISAs')]")).get(6).click();
			//driver.findElement(By.xpath("//a[contains(text(),'Enterprise')]")).click();
			Elements tbody = doc.getElementsByTag("tbody");
			Elements content = tbody.get(0).getElementsByTag("tr");
			Elements children = doc.children();
			for(Element ele : content) {
				Elements td = ele.children();
				String str[] = new String[3];
				int i = 0;
				if(!td.get(0).text().equals("open")) {
					for(Element childTd : td) {
						str[i] = childTd.text();
						i++;
					}
					listOfTuples.add(new Tuple(str[0],str[1],str[2]));
					System.out.println(str[0]+ " "+str[1]+" "+str[2]);
				}	
			}
			
			for(Tuple tuple : listOfTuples) {
				newObj.executeTuple(tuple);
				if(tuple.action.equals("clickAndWait") || tuple.action.equals("click")) {
					newObj.driver.findElement(By.xpath(tuple.target)).click();
				} else if(tuple.action.equals("type")) {
					newObj.driver.findElement(By.xpath(tuple.target)).sendKeys(tuple.data);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
