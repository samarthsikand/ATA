package com.cucumber.ATAImplement;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutomationATA {
	public static void main(String args[]) {
		String strLine;
		File file = new File("C:/Users/samarth_sikand/Desktop/newTest.html");
		try {
			Document doc = Jsoup.parse(file,"UTF-8");
			Elements content = doc.getElementsByTag("tr");
			for(Element ele : content) {
				Elements rows = ele.getElementsByTag("td");
				for(Element columns : rows) {
					System.out.println(columns.text());
				}
			}
		} catch(Exception e) {
			
		}
		
		
	}
}
