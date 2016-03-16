package com.cucumber.ATAImplement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainATA {
	
	/*public static void main(String args[]) {
		readFile();
	}*/
	
	public void readFile(String featureFilePath) {
		String strLine;
		Map<String,String> functions = new HashMap<String,String>();
		
		try {
			File file = new File("src/main/java/com/cucumber/ATAImplement/Implement.java");
			
			if(!file.exists()){
    			file.createNewFile();
    		} else {
    			file.delete();
    		}
			//System.out.println(file.getName());
			FileWriter fileWritter = new FileWriter("src/main/java/com/cucumber/ATAImplement/"+file.getName(),true);
	        //BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			fileWritter.write(getString("Package Name"));
			fileWritter.write(getString("Header Files"));
			fileWritter.write(getString("Class Name"));
			fileWritter.write("\t"+getString("Firefox driver"));
			fileWritter.write("\t"+getString("URL initialization"));
			fileWritter.write("\t"+getString("webelement initialization"));
			fileWritter.write("\t"+getString("Main Function"));
			FileInputStream fstream = new FileInputStream(featureFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			while ((strLine = br.readLine()) != null)   {
				  // Print the content on the console
				String codeLine = "";
				strLine = strLine.trim();
				if(strLine.length() > 0) {
					System.out.println (strLine);
					strLine = strLine.substring(strLine.indexOf(" ")).trim();
					System.out.println (strLine);
					if(strLine.startsWith("I am on")) {
						System.out.println(strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"')));
						codeLine = "\t navigateToWebsite();\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void navigateToWebsite() {\n"+
									"\t\t driver.manage().window().maximize();\n"+
									"\t\t driver.navigate().to(\"http://"+strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"'))+"\");\n"+
									"\t }\n";
						if(!functions.containsKey("navigation")) {
							functions.put("navigation", codeLine);
						}
						//fileWritter.write(codeLine);
					} else if(strLine.contains("set") && strLine.contains("field")) {
						System.out.println(strLine.split("\"")[1]+" "+strLine.split("\"")[3]);
						codeLine = "\t insertIntoFieldValues(\""+strLine.split("\"")[1]+"\",\""+strLine.split("\"")[3]+"\");\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void insertIntoFieldValues(String field,String value) {\n" + 
									"\t\t ele = driver.findElement(By.xpath(\"//label[contains(text(),'\"+field+\"')]/following::input[1]\"));\n" +
									"\t\t if(ele.getAttribute(\"readonly\") != null && ele.getAttribute(\"readonly\").equals(\"true\")) {\n"+
									"\t\t\t ((JavascriptExecutor)driver).executeScript(\"document.getElementById('\"+ele.getAttribute(\"id\")+\"').removeAttribute('readonly');\");\n" +
									"\t\t\t ele.clear();\n" +
									"\t\t\t ele.sendKeys(value);\n"+
									"\t\t } else {\n" +
									"\t\t\t ele.sendKeys(value);\n" + 
									"\t\t}\n" + 
									"\t}\n";
						if(!functions.containsKey("insertIntoFieldValues")) {
							functions.put("insertIntoFieldValues", codeLine);
						}
					} else if(strLine.contains("click") && strLine.contains("button")){
						System.out.println(strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"')));
						codeLine  = "\t clickButton(\""+strLine.substring(strLine.indexOf('"')+1, strLine.lastIndexOf('"'))+"\");\n";
						fileWritter.write(codeLine);
						codeLine = "\t public static void clickButton(String button) {\n" + 
									"\t\t String newXpath = \"//*[contains(text(),'\"+button+\"')]\";\n"+
									"\t\t List<WebElement> listEle = driver.findElements(By.xpath(newXpath));\n" +
									"\t\t for(WebElement ele : listEle) {\n" +
									"\t\t\t if(ele.getAttribute(\"class\").contains(\"button\")) {\n"+
									"\t\t\t\t ele.click();\n"+
									"\t\t\t\t break;\n" +
									"\t\t\t }\n" +
									"\t\t}\n" +
									"\t}\n";
						if(!functions.containsKey("clickButton")) {
							functions.put("clickButton", codeLine);
						}
					}
				}
				System.out.println (strLine);
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
	
	public String getString(String codeLine) {
		String headerFiles = "import java.util.HashMap; \n" + 
					  		 "import java.util.List; \n"+
					  		 "import java.util.Map; \n"+
					  		 "import org.openqa.selenium.By; \n"+
					  		 "import org.openqa.selenium.JavascriptExecutor; \n"+
					  		 "import org.openqa.selenium.WebDriver; \n"+
					  		 "import org.openqa.selenium.WebElement; \n"+
					  		 "import org.openqa.selenium.firefox.FirefoxDriver;\n\n";
		String packageName = "package com.cucumber.ATAImplement;\n\n";
		String className = "public class Implement {\n\n";
		String webDriverName = "private static WebDriver driver = new FirefoxDriver();\n";
		String privateStringURL = "private String URL;\n";
		String mainFunction = "public static void main(String args[]) {\n";
		String webElement = "private static WebElement ele = null;\n\n";
		
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
		} else {
			return null;
		}
	}
}
