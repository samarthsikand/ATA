package com.cucumber.ATAImplement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.cucumber.ATAImplement.Tree.Node;
import com.cucumber.ATAImplement.Tree.StackNode;

public class MainQV {
	public Stack<StackNode<WebElement>> stack = new Stack<StackNode<WebElement>>();
	public Map<String,StackNode<WebElement>> mapLabelToNode = new HashMap<String,StackNode<WebElement>>();
	public Map<WebElement,Node<WebElement>> mapElementToNode = new HashMap<WebElement,Node<WebElement>>();
	public List<String> labels = new ArrayList();
	public String URL = "";
	public WebDriver driver = null;
	public Node<WebElement> rootNode = null;
	
	/*public  void main(String args[]) {
		driver = new FirefoxDriver();
		//driver.navigate().to("file:///C:/Users/samarth_sikand/Desktop/test.html");
		driver.navigate().to("http://amazon.in");
		driver.findElement(By.id("nav-link-yourAccount")).click();
		driver.findElement(By.id("ap_email")).sendKeys("sammyman1691@yahoo.com");
		driver.findElement(By.id("ap_password")).sendKeys("voldemortz91");
		driver.findElement(By.id("signInSubmit")).click();
		WebElement hoverElement = driver.findElement(By.id("nav-link-yourAccount"));
		Actions builder = new Actions(driver);
		builder.moveToElement(hoverElement).perform();
		driver.findElement(By.id("nav_prefetch_yourorders")).click();
		List<WebElement> listOfElement = driver.findElements(By.xpath("//html"));
		Node<WebElement> rootNode = null;
		
		//Creating DOM tree
		for(WebElement ele : listOfElement) {
			rootNode = new Node<WebElement>(ele,null);
			System.out.println(ele.getTagName());
			createChildNodes(ele,1,rootNode);
		}
		//System.out.println("-----Tree-----");
		//printChildNodes(rootNode,1);
		
		
		labels.add("Return or replace items");
		labels.add("Delivered 14-Oct-15");
		
		//Searching for target labels and anchor labels
		System.out.println("Searching the labels..");
		for(String str : labels) {
			searchLabel(rootNode,str);
		}
		
		//Pushing Map elements to Stack
		System.out.println("Pushing into stack..");
		pushMapElementsToStack();
		
		//Filtering out instances
		WebElement ele = runningFilterInstances();
		ele.click();
		
		//System.out.println(listOfElement.size());
	}*/
	
	public MainQV() {
		
	}
	
	public MainQV(WebDriver driver) {
		this.driver = driver;
	}
	
	public void checkIfURLChanged(List<WebElement> listOfElement) {
		if(!URL.equals(driver.getCurrentUrl())) {
			System.out.println("URL is not the same");
			URL = driver.getCurrentUrl();
			mapElementToNode.clear();
			System.out.println("Creating child nodes..!");
			for(WebElement ele : listOfElement) {
				rootNode = null;
				rootNode = new Node<WebElement>(ele,null);
				System.out.println(ele.getTagName());
				createChildNodes(ele,1,rootNode);
			}
		}
	}
	
	private void clearAll() {
		mapLabelToNode.clear();
		while(!stack.isEmpty()) {
			stack.pop();
		}
	}
	
	public WebElement returnWebElement(List<String> listLabels) {
		List<WebElement> listOfElement = driver.findElements(By.xpath("//html"));
		clearAll();
		checkIfURLChanged(listOfElement);
		System.out.println("Searching the labels..");
		for(String str : listLabels) {
			System.out.println("Searching label: "+str);
			searchLabel(rootNode,str);
		}
		
		//Pushing Map elements to Stack
		System.out.println("Pushing into stack..");
		pushMapElementsToStack(listLabels);
		
		//Filtering out instances
		WebElement ele = runningFilterInstances();
		return ele;
	}
	
	public void createDOMTree() {
		Node<String> node = new Node<String>();
		node.data = "Hello World";
	}
	
	public Node<WebElement> createDOMNode() {
		Node<WebElement> newNode = new Node<WebElement>();
		return newNode;
	}
	
	public void printChildNodes(Node<WebElement> node,int i) {
		int j = 0;
		while(j < i) {
			System.out.print(" ");
			j++;
		}
		System.out.println(node.data.getTagName());
		if(node.data.getText().equalsIgnoreCase("Write a product review")) {
			System.out.print(" " + node.data.getText());
		}
		for(Node<WebElement> ele : node.children) {
			printChildNodes(ele,i+1);
		}
	}
	
	public void searchLabel(Node<WebElement> node,String label) {
		if(node.data.getText().equalsIgnoreCase(label)) {
			System.out.println("Found the label.. "+ label);
			if(!mapLabelToNode.containsKey(label)) {
				mapLabelToNode.put(label, new StackNode<WebElement>(label));
			}
			mapLabelToNode.get(label).listInstances.add(node.data);
			System.out.println("Pushed label into map "+label);
		} else {
			for(Node<WebElement> childNode : node.children) {
				searchLabel(childNode,label);
			}
		}
	}
	
	public void pushMapElementsToStack(List<String> listLabels) {
		for(String str : listLabels) {
			System.out.println("Pushing " + str);
			stack.push(mapLabelToNode.get(str));
			System.out.println(stack.peek().label+ " Size:"+stack.size());
		}
	}
	
	public  WebElement runningFilterInstances() {
		while(true) {
			StackNode<WebElement> stackNode1 = stack.pop();
			StackNode<WebElement> stackNode2 = stack.pop();
			System.out.println("Running filter instances...");
			System.out.println("Stack Node 1: "+stackNode1.label);
			System.out.println("Stack Node 2: "+stackNode2.label);
			filterInstances(stackNode1,stackNode2);
			if(stack.size() == 0) {
				if(stackNode2.listInstances.size() == 1) {
					System.out.println("Found the element..woohoo");
					return stackNode2.listInstances.get(0);
				}
				return null;
			}
			stack.push(stackNode2);
		}
		//return null;
	}
	
	public void filterInstances(StackNode<WebElement> stackNode1, StackNode<WebElement> stackNode2) {
		int[][] mat = new int[stackNode1.listInstances.size()][stackNode2.listInstances.size()];
		int maxval[] = new int[stackNode1.listInstances.size()];
		List<WebElement> listFiltered = new ArrayList<WebElement>();
		System.out.println("StackNode1 Instances: "+stackNode1.listInstances.size());
		System.out.println("StackNode2 Instances: "+stackNode2.listInstances.size());
		for(int i = 0; i < stackNode1.listInstances.size(); i++) {
			for(int j = 0; j < stackNode2.listInstances.size();j++) {
				mat[i][j] = getCommonAncestor(stackNode1.listInstances.get(i),stackNode2.listInstances.get(j));
				if(mat[i][j] > maxval[i]) {
					maxval[i] = mat[i][j];
				}
			}
		}
		
		for(int i = 0; i < stackNode1.listInstances.size(); i++) {
			for(int j = 0; j < stackNode2.listInstances.size(); j++) {
				System.out.print(mat[i][j]+ " ");
				if(mat[i][j] == maxval[i]) {
					listFiltered.add(stackNode2.listInstances.get(j));
				}
			}
			System.out.print("\n");
		}
		stackNode2.listInstances.clear();
		stackNode2.listInstances.addAll(listFiltered);
	}
	
	public int getCommonAncestor(WebElement l1, WebElement l2) {
		int i = 0;
		int lca = 0;
		List<WebElement> listOfElements1 = returnListToRoot(mapElementToNode.get(l1));
		List<WebElement> listOfElements2 = returnListToRoot(mapElementToNode.get(l2));
		Collections.reverse(listOfElements1);
		Collections.reverse(listOfElements2);
		while(i < listOfElements1.size() && i < listOfElements2.size()) {
			if(listOfElements1.get(i) == listOfElements2.get(i)) {
				System.out.println("The ancestor has matched..");
				lca++;
			} else {
				break;
			}
			i++;
		}
		
		if(i == listOfElements1.size() || i == listOfElements2.size()) {
			lca = i;
		}
		
		return lca;
	}
	
	public List<WebElement> returnListToRoot(Node<WebElement> node) {
		List<WebElement> list = new ArrayList<WebElement>();
		while(node != null) {
			list.add(node.data);
			node = node.parent;
		}
		return list;
	}
	
	public void createChildNodes(WebElement ele,int i,Node<WebElement> parentNode) {
		int j = 0;
		List<WebElement> listChild = ele.findElements(By.xpath("*"));
		//System.out.println(listChild.size());
		for(WebElement webEle : listChild) {
			if(!(webEle.getTagName().equalsIgnoreCase("script") || webEle.getTagName().equalsIgnoreCase("style") || webEle.getTagName().equalsIgnoreCase("meta"))) {
				j=0;
				Node<WebElement> currentNode = new Node<WebElement>(webEle,parentNode);
				if(!mapElementToNode.containsKey(webEle)) {
					mapElementToNode.put(webEle, currentNode);
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
}
