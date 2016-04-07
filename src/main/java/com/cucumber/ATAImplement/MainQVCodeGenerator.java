package com.cucumber.ATAImplement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cucumber.ATAImplement.Tree.Node;

public class MainQVCodeGenerator {

	public void readFile(String featureFilePath) {
		String strLine;
		Map<String,String> functions = new HashMap<String,String>();
		List<String> labelsList = new ArrayList<String>();
		
		try {
			File file = new File("src/main/java/com/cucumber/ATAImplement/ImplementQV.java");
			if(!file.exists()){
    			file.createNewFile();
    		} else {
    			file.delete();
    		}
			FileWriter fileWritter = new FileWriter("src/main/java/com/cucumber/ATAImplement/"+file.getName(),true);
			fileWritter.write(getCode("Package Name"));
			fileWritter.write(getCode("Header Files"));
			fileWritter.write(getCode("Class Name"));
			fileWritter.write("\t"+getCode("Firefox driver"));
			fileWritter.write("\t"+getCode("URL initialization"));
			fileWritter.write("\t"+getCode("webelement initialization"));
			fileWritter.write("\t\t"+getCode("MainQV object"));
			fileWritter.write("\t"+getCode("Main function"));
			fileWritter.write("\t"+getCode("Element list Html tag"));
			/*fileWritter.write("\t"+getCode("webelement initialization"));*/
			fileWritter.write("\t"+getCode("Root Node"));
			
			fileWritter.write("\t"+getCode("Labels list"));
			FileInputStream fstream = new FileInputStream(featureFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			while((strLine = br.readLine()) != null) {
				String codeLine = "";
				String listLabels = "";
				strLine = strLine.trim();
				if(strLine.length() > 0) {
					System.out.println(strLine);
					strLine = strLine.substring(strLine.indexOf(" ")).trim();
					if(strLine.startsWith("I am on")) {
						System.out.println(strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"')));
						codeLine = "\t\t navigateToWebsite();\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void navigateToWebsite() {\n"+
									"\t\t driver.manage().window().maximize();\n"+
									"\t\t driver.navigate().to(\"http://"+strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"'))+"\");\n"+
									"\t }\n";
						if(!functions.containsKey("navigation")) {
							functions.put("navigation", codeLine);
						}
					} else if(strLine.contains("click") && strLine.contains("button")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels+"\""+str+"\",";
						}
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t clickButton(labels);\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void clickButton(List<String> labels) {\n"+
									"\t\t ele = qvObj.returnWebElement(labels);\n"+
									"\t\t ele.click();\n"+
									"\t}\n";
						if(!functions.containsKey("clickbutton")) {
							functions.put("clickbutton",codeLine);
						}
					} else if(strLine.contains("set") && strLine.contains("field")) {
						labelsList = getWebElementAnchors(strLine);
						for(int i = 0;i<labelsList.size()-1;i++) {
							listLabels = listLabels+"\""+labelsList.get(i)+"\",";
						}
						
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t insertIntoFieldValues(labels,\""+labelsList.get(labelsList.size()-1)+"\");\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void insertIntoFieldValues(List<String> labels,String value) {\n"+
									"\t\t ele = qvObj.returnWebElement(labels);\n" +
									"\t\t ele.sendKeys(value);\n"+
									"\t}\n";
						if(!functions.containsKey("setfield")) {
							functions.put("setfield",codeLine);
						}
					} else if(strLine.contains("click") && strLine.contains("field")) {
						labelsList = getWebElementAnchors(strLine);
						for(String str : labelsList) {
							listLabels = listLabels+"\""+str+"\",";
						}
						listLabels = listLabels.substring(0, listLabels.length()-1);
						codeLine = "\t\t labels = Lists.newArrayList("+listLabels+");\n"+
									"\t\t clickField(labels);\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void clickField(List<String> labels) {\n"+
									"\t\t ele = qvObj.returnWebElement(labels);\n"+
									"\t\t WebElement inputEle = ele.findElement(By.xpath(\"./descendant::input[1] | ./following::input[1]\"));\n"+
									"\t\t inputEle.click();\n"+
									"\t}\n";
						if(!functions.containsKey("clickfield")) {
							functions.put("clickfield",codeLine);
						}
					}
				}
			}
			fileWritter.write("\t}\n");
			
			for(String str : functions.keySet()) {
				fileWritter.write(functions.get(str));
				fileWritter.write("\n");
			}
			fileWritter.write("}");
			fileWritter.close();
			br.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getWebElementAnchors(String line) {
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(line);
		List<String> anchors = new ArrayList<String>();
		while(m.find()) {
			anchors.add(m.group(1));
		}
		return anchors;
	}
	
	public String getCode(String codeLine) {
		String headerFiles = "import java.util.HashMap; \n" + 
					  		 "import java.util.List; \n"+
					  		 "import java.util.Map; \n"+
					  		 "import java.util.ArrayList;\n"+
					  		 "import org.openqa.selenium.By; \n"+
					  		 "import org.openqa.selenium.JavascriptExecutor; \n"+
					  		 "import org.openqa.selenium.WebDriver; \n"+
					  		 "import org.openqa.selenium.WebElement; \n"+
					  		 "import org.openqa.selenium.firefox.FirefoxDriver;\n"+
					  		 "import com.cucumber.ATAImplement.Tree.Node;\n"+
					  		 "import com.google.common.collect.Lists;\n\n";
		String packageName = "package com.cucumber.ATAImplement;\n\n";
		String className = "public class ImplementQV {\n\n";
		String webDriverName = "private static WebDriver driver = new FirefoxDriver();\n";
		String privateStringURL = "private String URL;\n";
		String mainFunction = "public static void main(String args[]) {\n";
		String webElement = "private static WebElement ele = null;\n\n";
		String mainQv = "private static MainQV qvObj = new MainQV(driver);\n\n";
		String listOfElement = "List<WebElement> listOfElement = driver.findElements(By.xpath(\"//html\"));\n\n";
		String rootNode = "Node<WebElement> rootNode = null;\n";
		String labelsList = "List<String> labels = new ArrayList<String>();\n";
		
		if(codeLine.equalsIgnoreCase("Header files")) {
			return headerFiles;
		} else if(codeLine.equalsIgnoreCase("Package Name")) {
			return packageName;
		} else if(codeLine.equalsIgnoreCase("Class name")) {
			return className;
		} else if(codeLine.equalsIgnoreCase("Firefox Driver")) {
			return webDriverName;
		} else if(codeLine.equalsIgnoreCase("URL initialization")) {
			return privateStringURL;
		} else if(codeLine.equalsIgnoreCase("Main function")) {
			return mainFunction;
		} else if(codeLine.equalsIgnoreCase("webelement initialization")) {
			return webElement;
		} else if(codeLine.equalsIgnoreCase("MainQV object")){
			return mainQv;
		} else if(codeLine.equalsIgnoreCase("Element list Html tag")) {
			return listOfElement;
		} else if(codeLine.equalsIgnoreCase("Root Node")) {
			return rootNode;
		} else if(codeLine.equals("Labels list")){
			return labelsList;
		} else {
			return null;
		}
	}
}
