package com.zehret.ipportscanner;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.zehret.console.Console;
import org.zehret.console.util.ConsoleProperties;
import org.zehret.console.util.PL;

public class PortScanner {

	public static void main(String[] args) {
		// ******************************************
		// * SETUP CONSOLE. PRETTIER UI FOR OUTPUT. *
		// ******************************************
		ConsoleProperties.CONSOLE_TITLE = "IP PORT SCANNER";
		ConsoleProperties.DATE_TIME_PREFIX = ConsoleProperties.HHMMSSms;
		ConsoleProperties.AUTOSTART_EVENT_HANDLER = false;
		ConsoleProperties.CONSOLE_FONT = new Font("Courier New", Font.PLAIN, 14);
		ConsoleProperties.TEXT_COLOR = Color.white;
		ConsoleProperties.BACKGROUND_COLOR = Color.decode("#003399");
		ConsoleProperties.PROMPTING_CONSOLE = true;
		ConsoleProperties.SHOW_CMD_LINE = true;
				
		new Console();
		
		while(true) {
			PL.con("[1] Select Address",PL.INFO);
			PL.con("[2] All Addresses",PL.INFO);
			while(true) {
				String input = Console.prompt("Choice", Console.SHOWINPUTOPTION	, PL.INFO, false);
				if(input.equalsIgnoreCase("1")) {
					String address = Console.prompt("Address:", Console.SHOWINPUTOPTION);
					PL.con_encap("THIS PROCESS WILL SCAN " + address + " FOR ALL PORTS", '#', PL.INFO, false);
					while(true) {
						input = Console.prompt("Start process? (Y/N)", Console.SHOWINPUTOPTION, PL.INFO, true);
						if(input.equalsIgnoreCase("Y")) {
							Console.setupProgressPopup(0, false);
							PL.con("");
							Console.consoleWindow.setLastOutputFieldText("");
							for(int n = 0; n <= 65535; n++) {
								testConnection(address,n);
								Console.setProgress(((float)n/65535f)*100);
							}
							Console.closeProgressPopup();
							PL.con("Done.");
							break;
						} else if(input.equalsIgnoreCase("N")) {
							System.exit(0);
						} else {
							continue;
						}
					}
					
				} else if(input.equalsIgnoreCase("2")) { 
					
					PL.con_encap("THIS PROCESS WILL SCAN ALL IP ADDRESS FOR ALL PORTS", '#', PL.INFO, false);
					while(true) {
						input = Console.prompt("Start process? (Y/N)", Console.SHOWINPUTOPTION, PL.INFO, true);
						if(input.equalsIgnoreCase("Y")) {
							
							testAllAddresses();	
							break;
						} else if(input.equalsIgnoreCase("N")) {
							System.exit(0);
						} else {
							continue;
						}
					}
				} else {
					continue;
				}
			}
		}	
	}
	private static void testAllAddresses() {
		Console.setupProgressPopup(0,false);
		for(byte n1 = 0; n1 < 256; n1++) {
			for(byte n2 = 0; n2 < 256; n2++) {
				for(byte n3 = 0; n3 < 256; n3++) {
					for(byte n4 = 0; n4 < 256; n4++) {
						for(int port = 0; port <= 65535; port++) {
							Console.setProgress(((float)(n1+n2+n3+n4)/(256f*256f*256f*256f))*100f);
							PL.con("");
							Console.consoleWindow.setLastOutputFieldText("");
							testConnection(n1,n2,n3,n4,port);
						}
					}
				}
			}
		}
		PL.con("Done.");
		Console.closeProgressPopup();
		
	}
	private static void testConnection(byte oct1, byte oct2, byte oct3, byte oct4, int port) {
		testConnection(oct1,oct2,oct3,oct4,port);
	}
	private static void testConnection(String oct1, String oct2, String oct3, String oct4, int port) {
		testConnection(oct1 + "." + oct2 + "." + oct3 + "." + oct4,port);
	}
	private static void testConnection(String address, int port) {
		try 
		{
		  Socket s = new Socket();
		  s.setReuseAddress(true);
		  SocketAddress sa = new InetSocketAddress(address, port);
		  Console.consoleWindow.setLastOutputFieldText(address + ":" + port);
		  s.connect(sa, 100);
		  Console.consoleWindow.setLastOutputFieldText("Found connection @" + address + ":" + port);
		  PL.con("");
		  Console.consoleWindow.setLastOutputFieldText("");
		  s.close(); 
		 } 
		catch (IOException e) 
		{
			return;         
		} 
	}
	

	
}
