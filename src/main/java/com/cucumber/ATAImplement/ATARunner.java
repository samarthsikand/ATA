package com.cucumber.ATAImplement;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class ATARunner {
	
	private static String featureFilePath = "src/main/java/com/cucumber/ATAImplement/ata.feature";
	private static String csvFilePath = "C:/Users/samarth_sikand/Desktop/mantisbt.csv";
	
	public static void main(String args[]) {
//		runATA();
		ataDriver();
	}
	
	private static void runATA() {
		MainATA objATA = new MainATA();
		objATA.readFile(featureFilePath);
	}
	
	private static void ataDriver() {
		try {
			RuntimeATA driverATA = new RuntimeATA();
			List<String[]> tuples = new ArrayList<String[]>();
			CSVReader reader = new CSVReader(new FileReader(csvFilePath));
			String [] nextLine;
			
			while ((nextLine = reader.readNext()) != null) {
		        // nextLine[] is an array of values from the line
		        System.out.println(nextLine[0] + " 2." + nextLine[1] + " 3." + nextLine[2] + " 4." + nextLine[3]);
		        tuples.add(nextLine);
		     }
			
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
//			tuples.add(new String[]{"file select","select","D:/workspace/ATAImplement/src/main/java/com/cucumber/ATAImplement/Stack.java",""});
//			tuples.add(new String[]{"textbox","click","Date of Journey",""});
//			tuples.add(new String[]{"calendar","select","","Mar  2016,26",""});
//			tuples.add(new String[]{"textbox","click","Date of Return",""});
//			tuples.add(new String[]{"calendar","select","","Apr  2016,2",""});
			driverATA.runTheTuples(tuples);
		     
		} catch(Exception e) {
			
		}
		
	}
}
