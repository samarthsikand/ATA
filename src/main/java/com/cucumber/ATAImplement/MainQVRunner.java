package com.cucumber.ATAImplement;

public class MainQVRunner {

	private static String featureFilePath = "src/main/java/com/cucumber/ATAImplement/ata.feature";
	
	public static void main(String args[]) {
		MainQVCodeGenerator objQV = new MainQVCodeGenerator();
		objQV.readFile(featureFilePath);
	}
}
