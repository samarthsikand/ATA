package com.cucumber.ATAImplement;

import java.util.ArrayList;
import java.util.List;

public class ATARunner {
	
	private static String featureFilePath = "src/main/java/com/cucumber/ATAImplement/ata.feature";
	
	public static void main(String args[]) {
//		runATA();
		ataDriver();
	}
	
	private static void runATA() {
		MainATA objATA = new MainATA();
		objATA.readFile(featureFilePath);
	}
	
	private static void ataDriver() {
		RuntimeATA driverATA = new RuntimeATA();
		List<String[]> tuples = new ArrayList<String[]>();
		tuples.add(new String[]{"","goto","","http://www.w3schools.com/jsref/tryit.asp?filename=tryjsref_fileupload_get"});
		/*tuples.add(new String[]{"textbox","enter","From","Delhi"});
		tuples.add(new String[]{"textbox","enter","To","Jaipur"});
		tuples.add(new String[]{"textbox","click","Date of Journey",""});
		tuples.add(new String[]{"textbox","click","1",""});
		tuples.add(new String[]{"button","click","Search Buses",""});*/
		//tuples.add(new String[]{"radiobutton","select","in",""});
		//tuples.add(new String[]{"radiobutton group","select","Select pizza crust/deep",""});
		//tuples.add(new String[]{"multiselect","select","mushrooms,green peppers,onions",""});
		//tuples.add(new String[]{"dropdown","select","Data Mining",""});
		//tuples.add(new String[]{"alert","select","ok",""});
		tuples.add(new String[]{"file select","select","D:/workspace/ATAImplement/src/main/java/com/cucumber/ATAImplement/Stack.java",""});
		driverATA.runTheTuples(tuples);
	}
}
