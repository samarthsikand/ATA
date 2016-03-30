package com.cucumber.ATAImplement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.ls.LSInput;

import com.cucumber.ATAImplement.Tree.ANode;
import com.cucumber.ATAImplement.Tree.Node;

public class AutomationATA {
	public static Map<WebElement,ANode<WebElement>> mapWebElementToNode = new HashMap<WebElement,ANode<WebElement>>();
	public static WebDriver driver = null;
	public static String URL = "";
	public static ANode<WebElement> rootNode = null;
	
	public static void main(String args[]) {
		String strLine;
		File file = new File("C:/Users/samarth_sikand/Desktop/bellTest.html");
		try {
			Document doc = Jsoup.parse(file,"UTF-8");
			List<Tuple> listOfTuples = new ArrayList<Tuple>();
			driver.navigate().to("http://amazon.in");
			Elements content = doc.getElementsByTag("tr");
			Elements children = doc.children();
			for(Element ele : content) {
				Elements td = ele.children();
				String str[] = new String[3];
				int i = 0;
				for(Element childTd : td) {
					str[i] = childTd.text();
					i++;
				}
				listOfTuples.add(new Tuple(str[0],str[1],str[2]));
				System.out.println(str[0]+ " "+str[1]+" "+str[2]);
			}
			
			for(Tuple tuple : listOfTuples) {
				executeTuple(tuple);
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
	
	public static void checkIfURLChanged() {
		if(!URL.equals(driver.getCurrentUrl())) {
			List<WebElement> listOfElement = driver.findElements(By.xpath("//html"));
			System.out.println("URL is not the same");
			URL = driver.getCurrentUrl();
			mapWebElementToNode.clear();
			System.out.println("Creating child nodes..!");
			for(WebElement ele : listOfElement) {
				rootNode = null;
				rootNode = new ANode<WebElement>(ele,null);
				System.out.println(ele.getTagName());
				createChildNodes(ele,1,rootNode);
			}
		}
	}
	
	public static void executeTuple(Tuple tuple) {
		checkIfURLChanged();
		WebElement element = null;
		
		if(!tuple.action.equalsIgnoreCase("open")) {
			if(tuple.target.contains("=")) {
				tuple.target = tuple.target.substring(tuple.target.indexOf("="));
			}
			element = driver.findElement(By.xpath(tuple.target));
			generateAnchors(element);
		}
	}
	
	public static void generateAnchors(WebElement ele) {
		String label = "";
		List<WebElement> otherLabels = null;
		List<ANode<WebElement>> otherLabelTree = new ArrayList<ANode<WebElement>>();
		List<ANode<WebElement>> pathFromTargetToRoot = null;
		if(ele.getTagName().equals("a") || ele.getTagName().equals("button") || (ele.getTagName().equals("input") && ele.getAttribute("type").equals("submit"))) {
			label = ele.getText();
			otherLabels = driver.findElements(By.xpath("//*[text()='"+label+"']"));
		} else {
			label = ele.getTagName();
			otherLabels = driver.findElements(By.xpath("//"+label+""));
		}
		ANode<WebElement> subtreeTargetNode = getInterestingSubtree(otherLabels,ele);
		pathFromTargetToRoot = getPathFromTargetToRoot(ele);
	}
	
	public static ANode<WebElement> getInterestingSubtree(List<WebElement> otherLabels, WebElement ele) {
		mapWebElementToNode.get(ele).value = 1;
		for(WebElement element : otherLabels) {
			mapWebElementToNode.get(element).value = 1;
		}
		calculateValueOfNode(rootNode);
		
		ANode<WebElement> targetNode = mapWebElementToNode.get(ele);
		while(targetNode.parent.value == 1) {
			targetNode = targetNode.parent;
		}
		return targetNode;
	}
	
	public static List<ANode<WebElement>> getPathFromTargetToRoot(WebElement ele) {
		List<ANode<WebElement>> listFromTargetToNode = new ArrayList<ANode<WebElement>>();
		ANode<WebElement> node = mapWebElementToNode.get(ele);
		while(node.parent != null) {
			listFromTargetToNode.add(node);
			node = node.parent;
		}
		return listFromTargetToNode;
	}
	
	public static int calculateValueOfNode(ANode<WebElement> rootNode) {
		if(rootNode.children.size() == 0) {
			return rootNode.value;
		}
		for(ANode<WebElement> childNode : rootNode.children) {
			rootNode.value = rootNode.value + calculateValueOfNode(childNode);
		}
		return rootNode.value;
	}
	
	public static void createChildNodes(WebElement ele,int i,ANode<WebElement> parentNode) {
		int j = 0;
		List<WebElement> listChild = ele.findElements(By.xpath("*"));
		//System.out.println(listChild.size());
		for(WebElement webEle : listChild) {
			if(!(webEle.getTagName().equalsIgnoreCase("script") || webEle.getTagName().equalsIgnoreCase("style") || webEle.getTagName().equalsIgnoreCase("meta"))) {
				j=0;
				ANode<WebElement> currentNode = new ANode<WebElement>(webEle,parentNode);
				if(!mapWebElementToNode.containsKey(webEle)) {
					mapWebElementToNode.put(webEle, currentNode);
				}
				/*while(j < i) {
					System.out.print(" ");
					j++;
				}
				System.out.println(webEle.getTagName());*/
				createChildNodes(webEle,i+1,currentNode);
				//System.out.println(currentNode+ " " + currentNode.data.getTagName());
				parentNode.children.add(currentNode);
			}
		}
	}
	
	static class Tuple {
		String action;
		String target;
		String data;
		
		public Tuple() {
			
		}
		
		public Tuple(String action, String target, String data) {
			this.action = action;
			this.target = target;
			this.data = data;
		}
	}
}
