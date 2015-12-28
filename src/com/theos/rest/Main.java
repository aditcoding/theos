package com.theos.rest;

public class Main {
	
	public static void main(String[] args) {
		double i = 21.9;
		double j = 7;
		int b = 0;
		log(2/Math.PI);
	}
	
	static void log(Object... obj) {
		for (Object o : obj) {
			System.out.println(o);
		}
	}
}
