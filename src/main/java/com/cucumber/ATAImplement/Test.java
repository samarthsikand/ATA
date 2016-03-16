package com.cucumber.ATAImplement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class Test {
	
	public static void main(String args[]) {
		String str = "This sample \"string\" is very \"nice\"";
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(str);
		while (m.find()) {
		  System.out.println(m.group(1));
		}
		String tempStr = "\"label1\",\"label2\",";
		System.out.println(tempStr.substring(0, tempStr.length()-1));
		Lists.newArrayList();
	}
}
