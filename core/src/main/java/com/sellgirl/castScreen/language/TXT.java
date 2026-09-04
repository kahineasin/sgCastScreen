package com.sellgirl.castScreen.language;

import java.util.Map;

public class TXT{
	private static Map<String,String> map=null;
	public static void init(Map<String,String> map) {
		TXT.map=map;
	}
	public static String g(String s) {
		if(null==map) {return s;}
		if(map.containsKey(s)) {return map.get(s);}
		return s;
	}

}
