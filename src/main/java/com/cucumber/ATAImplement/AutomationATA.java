package com.cucumber.ATAImplement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cucumber.ATAImplement.Tree.ANode;

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
			List<String> listOfAnchors = generateAnchors(element);
		}
	}
	
	public static List<String> generateAnchors(WebElement ele) {
		String label = "";
		List<WebElement> otherLabels = null;
		List<String> listOfAnchors = new ArrayList<String>();
		Set<ANode<WebElement>> otherLabelTrees = new HashSet<ANode<WebElement>>();
		ANode<WebElement> otherTreeNode = null;
		ANode<WebElement> closestTree = null;
		List<ANode<WebElement>> listPathFromTargetToRoot = null;
		Set<ANode<WebElement>> setPathFromTargetToRoot = null;
		if(ele.getTagName().equals("a") || ele.getTagName().equals("button") || (ele.getTagName().equals("input") && ele.getAttribute("type").equals("submit"))) {
			label = ele.getText();
			otherLabels = driver.findElements(By.xpath("//*[text()='"+label+"']"));
		} else {
			label = ele.getTagName();
			otherLabels = driver.findElements(By.xpath("//"+label+""));
		}
		ANode<WebElement> subtreeTargetNode = getInterestingSubtree(otherLabels,ele);
		listPathFromTargetToRoot = getPathFromTargetToRoot(ele);
		setPathFromTargetToRoot.addAll(listPathFromTargetToRoot);
		
		for(WebElement otherEle : otherLabels) {
			otherTreeNode = getOtherSubTree(otherEle,setPathFromTargetToRoot);
			if(!otherLabelTrees.contains(otherTreeNode)) {
				otherLabelTrees.add(otherTreeNode);
			}
		}
		
		while(otherLabelTrees.size() != 0) {
			String distinctLabel = getDistinctLabel(subtreeTargetNode,otherLabelTrees);
			if(distinctLabel != null) {
				listOfAnchors.add(distinctLabel);
				return listOfAnchors;
			}
			closestTree = getClosestTree(subtreeTargetNode,otherLabelTrees,listPathFromTargetToRoot,setPathFromTargetToRoot);
			Set<ANode<WebElement>> closestTreeList = new HashSet<ANode<WebElement>>();
			closestTreeList.add(closestTree);
			distinctLabel = getDistinctLabel(subtreeTargetNode,closestTreeList);
			if(distinctLabel == null) {
				return null;
			}
			listOfAnchors.add(distinctLabel);
			subtreeTargetNode = mergeSubtrees(subtreeTargetNode,closestTree,otherLabelTrees);
		}
		
		return null;
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
	
	public static ANode<WebElement> getOtherSubTree(WebElement element, Set<ANode<WebElement>> pathFromTargetToRoot) {
		ANode<WebElement> otherNode = mapWebElementToNode.get(element);
		while(otherNode.parent != null && !pathFromTargetToRoot.contains(otherNode.parent)) {
			otherNode = otherNode.parent;
		}
		return otherNode;
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
	
	public static String getDistinctLabel(ANode<WebElement> subtreeTargetNode, Set<ANode<WebElement>> otherLabelTrees) {
		Set<String> listOfLabelsTargetTree = new HashSet<String>();
		List<String> targetSubtreeLabels = getLabelsOfTree(subtreeTargetNode);
		List<String> listOtherSubtreeLabels = getLabelsOfTree(otherLabelTrees);
		Set<String> setOtherSubtreeLabels = new HashSet(listOtherSubtreeLabels);
		
		for(String str : targetSubtreeLabels) {
			if(setOtherSubtreeLabels.contains(str)) {
				return str;
			}
		}
		
		return null;
	}
	
	public static List<String> getLabelsOfTree(ANode<WebElement> subTreeTargetNode) {
		List<String> labels = new ArrayList<String>();
		List<WebElement> listLabels = subTreeTargetNode.data.findElements(By.xpath("./descendant::h1 | ./descendant::h2 | ./descendant::h3 | ./descendant::h3 | ./descendant::h4"));
		if(listLabels.size() != 0) {
			for(WebElement ele : listLabels) {
				if(!labels.contains(ele.getText())) {
					labels.add(ele.getText());
				}
			}
		}
		
		listLabels = subTreeTargetNode.data.findElements(By.xpath("./descendant::label | ./descendant::td"));
		if(listLabels.size() != 0) {
			for(WebElement ele : listLabels) {
				if(!labels.contains(ele.getText()) || !ele.getText().equals("")) {
					labels.add(ele.getText());
				}
			}
		}
		
		listLabels = subTreeTargetNode.data.findElements(By.xpath("./descendant::span"));
		if(listLabels.size() != 0) {
			for(WebElement ele : listLabels) {
				if(!labels.contains(ele.getText()) || !ele.getText().equals("")) {
					labels.add(ele.getText());
				}
			}
		}
		
		return labels;
	}
	
	public static List<String> getLabelsOfTree(Set<ANode<WebElement>> otherLabelTrees) {
		List<String> labels = new ArrayList<String>();
		for(ANode<WebElement> nodeEle : otherLabelTrees) {
			labels.addAll(getLabelsOfTree(nodeEle));
		}
		return labels;
	}
	
	public static ANode<WebElement> getClosestTree(ANode<WebElement> subTreeTargetNode, Set<ANode<WebElement>> otherLabelTrees, List<ANode<WebElement>> listFromTargetToRoot, Set<ANode<WebElement>> setOfTargetToRoot) {
		ANode<WebElement> nodeEle = null;
		ANode<WebElement> closestSubtreeNode = null;
		int dist = 0;
		int minDist = 9999999;
		for(ANode<WebElement> node : otherLabelTrees) {
			nodeEle = node;
			dist=0;
			while(nodeEle.parent != null || setOfTargetToRoot.contains(nodeEle.parent)) {
				nodeEle = nodeEle.parent;
			}
			if(setOfTargetToRoot.contains(nodeEle.parent)) {
				dist = listFromTargetToRoot.indexOf(nodeEle.parent);
			}
			if(dist < minDist) {
				minDist = dist;
				closestSubtreeNode = node;
			}
		}
		return closestSubtreeNode;
	}
	
	public static ANode<WebElement> mergeSubtrees(ANode<WebElement> subtreeTarget, ANode<WebElement> closestTree, Set<ANode<WebElement>> otherLabelTrees) {
		List<ANode<WebElement>> pathOfTargetToRoot = getPathFromTargetToRoot(subtreeTarget.data);
		List<ANode<WebElement>> pathOfClosestTreeToRoot = getPathFromTargetToRoot(closestTree.data);
		Map<ANode<WebElement>,Set<ANode<WebElement>>> mapOtherNodeToPaths = new HashMap<ANode<WebElement>,Set<ANode<WebElement>>>();
		
		for(ANode<WebElement> node : otherLabelTrees) {
			if(!mapOtherNodeToPaths.containsKey(node)) {
				mapOtherNodeToPaths.put(node, new HashSet<ANode<WebElement>>());
			}
			Set<ANode<WebElement>> setOfNodes = new HashSet<ANode<WebElement>>();
			setOfNodes.addAll(getPathFromTargetToRoot(node.data));
			mapOtherNodeToPaths.put(node, setOfNodes);
		}
		
		return new ANode<WebElement>();
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
