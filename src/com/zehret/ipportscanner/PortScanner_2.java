package com.zehret.ipportscanner;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.zehret.console.Console;
import org.zehret.console.util.ConsoleConfiguration;
import org.zehret.console.util.PL;

public class PortScanner_2 {
	private static int timeoutMS = 500;
	private static int reportOption = 2;
	private static ReportData DATA;
	private static ArrayList<ArrayList<Short>> addresses;
	
	public static void main(String[] args) {
		// ******************************************
		// * SETUP CONSOLE. PRETTIER UI FOR OUTPUT. *
		// ******************************************
		ConsoleConfiguration.CONSOLE_TITLE = "IP Port Scanner";
		ConsoleConfiguration.DATE_TIME_PREFIX = "";
		//ConsoleConfiguration.DATE_TIME_PREFIX = ConsoleConfiguration.HHMMSSms;
		ConsoleConfiguration.AUTOSTART_EVENT_HANDLER = false;
		ConsoleConfiguration.CONSOLE_FONT = new Font("Consolas", Font.PLAIN, 20);
		ConsoleConfiguration.TEXT_COLOR = Color.white;
		ConsoleConfiguration.BACKGROUND_COLOR = Color.decode("#003399");
		ConsoleConfiguration.PROMPTING_CONSOLE = true;
		ConsoleConfiguration.SHOW_CMD_LINE = true;
		ConsoleConfiguration.SHOW_ENTRY_IDENTIFIER = false;
		new Console();
		DATA = new ReportData();
		
		boolean exit = false;
		while(!exit) {
			//int selection = menuPrompt();
			
			
		}
	}
	private static void compileAddresses() {
		
	}
	
	
	private static ArrayList<ArrayList<Short>> GET_ADDRESS_OCTETS(short oct1, boolean o1, short oct2, boolean o2, short oct3, boolean o3, short oct4, boolean o4) {
		//PL.con(oct1 + ", " + o1 + ", " + oct2 + ", " + o2 + ", " + oct3 + ", " + o3 + ", " +oct4 + ", " + o4);
		ArrayList<ArrayList<Short>> ADDRESS = new ArrayList<ArrayList<Short>>();
		if(o1) {
			ADDRESS.add(0,new ArrayList<Short>());
			for(short n = 1; n < 256; n++) {
				ADDRESS.get(0).add(n);
			}
		} else {
			ADDRESS.add(0,new ArrayList<Short>());
			ADDRESS.get(0).add(oct1);
		}
		if(o2) {
			ADDRESS.add(1,new ArrayList<Short>());
			for(short n = 1; n < 256; n++) {
				ADDRESS.get(1).add(n);
			}
		} else {
			ADDRESS.add(1,new ArrayList<Short>());
			ADDRESS.get(1).add(oct2);
		}
		if(o3) {
			ADDRESS.add(2,new ArrayList<Short>());
			for(short n = 1; n < 256; n++) {
				ADDRESS.get(2).add(n);
			}
		} else {
			ADDRESS.add(2,new ArrayList<Short>());
			ADDRESS.get(2).add(oct3);
		}
		if(o4) {
			ADDRESS.add(3,new ArrayList<Short>());
			for(short n = 1; n < 256; n++) {
				ADDRESS.get(3).add(n);
			}
		} else {
			ADDRESS.add(3,new ArrayList<Short>());
			ADDRESS.get(3).add(oct4);
		}
		long count = 1;
		for(int n = 0; n < ADDRESS.size(); n++) {
			count*=ADDRESS.get(n).size();
		}
		PL.con("Generated " + count + " addresses.", PL.NONE);	
		return ADDRESS;		
	}
}
