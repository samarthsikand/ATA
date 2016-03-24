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
			//Document doc = Jsoup.parse(file,"UTF-8");
			Document doc = Jsoup.connect("http://amazon.in/").get();
			//Elements content = doc.getElementsByTag("tr");
			Elements children = doc.children();
			for(Element ele : children) {
				System.out.println(ele.tagName());
				printAllChildren(ele,0);
			}
		} catch(Exception e) {
			
		}
	}
	
	public static void printAllChildren(Element root, int i) {
		Elements rows = root.children();
		for(Element columns : rows) {
			for(int j=0; j<i; j++) {
				System.out.print(" ");
			}
			System.out.println(columns.tagName());
			printAllChildren(columns,i+1);
		}
	}
}
