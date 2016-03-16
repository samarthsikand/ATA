package com.cucumber.ATAImplement;

public class ATARunner {
	
	private static String featureFilePath = "src/main/java/com/cucumber/ATAImplement/ata.feature";
	
	public static void main(String args[]) {
		MainATA objATA = new MainATA();
		objATA.readFile(featureFilePath);
	}
}
