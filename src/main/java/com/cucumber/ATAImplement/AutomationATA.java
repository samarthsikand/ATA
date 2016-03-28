package com.cucumber.ATAImplement;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cucumber.ATAImplement.Tree.ANode;

public class AutomationATA {
	public static void main(String args[]) {
		String strLine;
		File file = new File("C:/Users/samarth_sikand/Desktop/newTest.html");
		try {
			Document doc = Jsoup.parse(file,"UTF-8");
			//Document doc = Jsoup.connect("http://amazon.in/").get();
			//Elements content = doc.getElementsByTag("tr");
			Elements children = doc.children();
			for(Element ele : children) {
				System.out.println(ele.tagName());
				ANode<Element> rootNode = new ANode(ele,null);
				printAllChildren(rootNode,0);
			}
		} catch(Exception e) {
			
		}
	}
	
	public static void printAllChildren(ANode<Element> root, int i) {
		Elements rows = root.data.children();
		for(Element columns : rows) {
			if(!(columns.tagName().equalsIgnoreCase("script") || columns.tagName().equalsIgnoreCase("style") || columns.tagName().equalsIgnoreCase("meta"))) {
				ANode<Element> node = new ANode(columns,root);
				for(int j=0; j<i; j++) {
					System.out.print(" ");
				}
				System.out.println(columns.text());
				printAllChildren(node,i+1);
			}
		}
	}
}
