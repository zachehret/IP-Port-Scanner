package com.zehret.ipportscanner;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import org.zehret.console.Console;
import org.zehret.console.util.ConsoleProperties;
import org.zehret.console.util.PL;

public class PortScanner {

	private static int timeoutMS = 100;
	
	public static void main(String[] args) {
		// ******************************************
		// * SETUP CONSOLE. PRETTIER UI FOR OUTPUT. *
		// ******************************************
		ConsoleProperties.CONSOLE_TITLE = "IP PORT SCANNER";
		ConsoleProperties.DATE_TIME_PREFIX = ConsoleProperties.HHMMSSms;
		ConsoleProperties.AUTOSTART_EVENT_HANDLER = false;
		ConsoleProperties.CONSOLE_FONT = new Font("Courier New", Font.PLAIN, 16);
		ConsoleProperties.TEXT_COLOR = Color.white;
		ConsoleProperties.BACKGROUND_COLOR = Color.decode("#003399");
		ConsoleProperties.PROMPTING_CONSOLE = true;
		ConsoleProperties.SHOW_CMD_LINE = true;
		ConsoleProperties.SHOW_ENTRY_IDENTIFIER = false;
				
		new Console();
		
		while(true) {
			while(true) {
				PL.con("########## MAIN ##########");
				PL.con("# [1] Select Address.....#", PL.INFO);
				PL.con("# [2] All Addresses......#", PL.INFO);
				PL.con("# [3] Modify Settings....#", PL.INFO);
				PL.con("# [4] Exit...............#", PL.INFO);
				PL.con("##########################", PL.INFO);
				String input = Console.prompt("Option", Console.SHOWINPUTOPTION	, PL.INFO, false);
				ArrayList<Integer> ports = new ArrayList<Integer>();
				if(input.equalsIgnoreCase("1")) {
					String address;
					while(true) {
						try {
							address = Console.prompt("Address:", Console.SHOWINPUTOPTION);
							String copy = address;
							if(validate(address.replaceAll("x", "0"))) {
								address = copy;
								break;
							}
							throw new IllegalArgumentException();
						}catch(IllegalArgumentException e) {
							PL.con("Invalid address");
						}
					}
					ports = promptForPortSelection();
					
					
					//PL.con(octets.length + "");
					PL.con_encap("THIS PROCESS WILL SCAN " + address + " FOR THE DESIRED PORTS", '#', PL.INFO, false);
					
					while(true) {
						input = Console.prompt("Start process? (Y/N)", Console.SHOWINPUTOPTION, PL.INFO, true);
						if(input.equalsIgnoreCase("Y")) {
							//PL.con(address);
							String[] octets = address.split("\\.");
							boolean o1 = false, o2 = false, o3 = false, o4 = false;
							short oct1 = -1, oct2 = -1, oct3 = -1, oct4 = -1;
							
							if(octets[0].contains("x")) {
								o1 = true;
							} else {
								try {
									oct1 = Short.parseShort(octets[0]);
								}catch(NumberFormatException e) {}
							}
							if(octets[1].contains("x")) {
								o2 = true;
							} else {
								try {
									oct2 = Short.parseShort(octets[1]);
								}catch(NumberFormatException e) {}
							}
							if(octets[2].contains("x")) {
								o3 = true;
							} else {
								try {
									oct3 = Short.parseShort(octets[2]);
								}catch(NumberFormatException e) {}
							}
							if(octets[3].contains("x")) {
								o4 = true;
							} else {
								try {
									oct4 = Short.parseShort(octets[3]);
								}catch(NumberFormatException e) {}
							}
							
						//	PL.con(octets[0] + ">" + octets[1] + ">" + octets[2] + ">" + octets[3]);
							
							
							Console.setupProgressPopup(0, false);
							PL.con("Generating addresses..");
							ArrayList<ArrayList<Short>> addresses = GET_ADDRESS_OCTETS(oct1, o1, oct2, o2, oct3, o3, oct4, o4);

							PL.con("");
							Console.consoleWindow.setLastOutputFieldText("");
							for(int n1 = 0; n1 < addresses.get(0).size(); n1++) {
								for(int n2 = 0; n2 < addresses.get(1).size(); n2++) {
									for(int n3 = 0; n3 < addresses.get(2).size(); n3++) {
										for(int n4 = 0; n4 < addresses.get(3).size(); n4++) {
											for(int n = 0; n < ports.size(); n++) {
												testConnection(addresses.get(0).get(n1),addresses.get(1).get(n2),addresses.get(2).get(n3),addresses.get(3).get(n4),ports.get(n));
												Console.setProgress(((float)n/ports.size())*100);
											}
										}
									}
								}
							}
							
							
						//	for(int n = 0; n <= ports.size(); n++) {
						//		testConnection(octets[0],octets[1],octets[2],octets[3],ports.get(n));
						//		Console.setProgress(((float)n/ports.size())*100);
						//	}
							Console.closeProgressPopup();
							PL.con("Done.");
							try {
								Thread.sleep(1000);
							}catch(Exception e) {}
							break;
						} else if(input.equalsIgnoreCase("N")) {
							break;
						} else {
							continue;
						}
					}
					
				} else if(input.equalsIgnoreCase("2")) { 
					ports = promptForPortSelection();
					PL.con_encap("THIS PROCESS WILL SCAN ALL IP ADDRESS FOR DESIRED PORTS", '#', PL.INFO, false);
					while(true) {
						input = Console.prompt("Start process? (Y/N)", Console.SHOWINPUTOPTION, PL.INFO, true);
						if(input.equalsIgnoreCase("Y")) {
							testAllAddresses(ports);	
							break;
						} else if(input.equalsIgnoreCase("N")) {
							break;
						} else {
							continue;
						}
					}
				} else if(input.equalsIgnoreCase("3")) {
					while(true) {
						try {
							PL.con("###############################");
							PL.con("# [1] ... Modify Timeout Time #");
							PL.con("# [2] ... Exit                #");
							PL.con("###############################");
							String settingsPrompt = Console.prompt("Option: ", Console.SHOWINPUTOPTION);
							if(settingsPrompt.equalsIgnoreCase("1")) {
								timeoutMS = Integer.parseInt(promptForSetting("Modify timeout time (Current value: " + timeoutMS + ")","This value adjusts the amount of time taken to attempt to connect to an address/port. Default value: 100ms"));	
							} else if(settingsPrompt.equalsIgnoreCase("2")) {
								try {
									Thread.sleep(1000);
								}catch(Exception e) {}
								break;
							} else {
								continue;
							}
							PL.con("# Setting Updated #");
							try {
								Thread.sleep(1000);
							}catch(Exception e) {}
						}catch(Exception e) { }
					}
				} else if(input.equalsIgnoreCase("4")) {
					return;
				} else {
					continue;
				}
			}
		}	
	}
	private static String promptForSetting(String message, String tip) {
		
		PL.con_encap(message + "(" + tip + ")", '#', PL.INFO, PL.NO_ALERT);
		
		return Console.prompt("Set value", Console.SHOWINPUTOPTION);
	}
	private static ArrayList<Integer> promptForPortSelection() {
		while(true) {
			try {
				String ports = Console.prompt("Enter the ports to scan (Separate values and ranges by commas. E.g. 3,22-80,3000.)", Console.SHOWINPUTOPTION);
				ports.replaceAll("\\s","");
				if((ports.startsWith("x"))||(ports.startsWith("X"))) {
					return parsePortSelection("0-65535");
				}
				return parsePortSelection(ports);
			}catch(Exception e) {
				PL.con("Invalid Data");
			}
		}
	}
	private static ArrayList<Integer> parsePortSelection(String portsInput) {
		ArrayList<Integer> portRange = new ArrayList<Integer>();
		String[] portsAndRanges = portsInput.split(",");
		for(int n = 0; n < portsAndRanges.length; n++) {
			if(portsAndRanges[n].contains("-")) {
				int start = Integer.parseInt(portsAndRanges[n].split("-")[0]);
				int end = Integer.parseInt(portsAndRanges[n].split("-")[1]);
				for(int i = start; i <= end; i++) {
					portRange.add(i);
				}
			} else {
				int port = Integer.parseInt(portsAndRanges[n]);
				if((port < 0)||(port > 65535))
					throw new IllegalArgumentException("Port out of range");
				portRange.add(port);
			}
		}
//		String ports = "";
//		for(int n = 0; n < portRange.size(); n++) {
//			ports += portRange.get(n) + ", ";
//		}
//		PL.con(ports);
		return portRange;
	}
	private static void testAllAddresses(ArrayList<Integer> ports) {
		Console.setupProgressPopup(0,false);
		PL.con("");
		Console.consoleWindow.setLastOutputFieldText("");
		for(short n1 = 1; n1 < 256; n1++) {
			for(short n2 = 1; n2 < 256; n2++) {
				for(short n3 = 1; n3 < 256; n3++) {
					for(short n4 = 1; n4 < 256; n4++) {
						for(int p = 0; p < ports.size(); p++) {
							Console.setProgress(((float)(n1+n2+n3+n4+p)/(256f*256f*256f*256f*ports.size()))*100f);
							testConnection(n1,n2,n3,n4,ports.get(p));
						}
					}
				}
			}
		}
		PL.con("Done.",PL.INFO,PL.ALERT);
		Console.closeProgressPopup();
		try {
			Thread.sleep(1000);
		}catch(Exception e) {}
		
	}
	private static void testConnection(short oct1, short oct2, short oct3, short oct4, int port) {
		testConnection(oct1 + "",oct2 + "",oct3 + "",oct4 + "",port);
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
		  s.connect(sa, timeoutMS);
		  Console.consoleWindow.setLastOutputFieldText("Found connection @" + address + ":" + port);
		  PL.con("");
		  Console.consoleWindow.setLastOutputFieldText("");
		  s.close(); 
		}catch (NoRouteToHostException e) {
			while(true) {
				ConsoleProperties.MSG_CUSTOM_COLOR = Color.yellow;
				ConsoleProperties.BACKGROUND_CUSTOM_COLOR = Color.red;
				PL.con("Device Offline.",PL.WARNC);
				String retry = Console.prompt("Retry? (Y/N)", Console.SHOWINPUTOPTION, PL.WARNC, true);
				if(retry.equalsIgnoreCase("Y")) {
					testConnection(address,port);
					break;
				} else if(retry.equalsIgnoreCase("N")) {
					return;
				} else {
					continue;
				}
			}
			
		}
		catch (IOException e) 
		{
			//e.printStackTrace();
			return;         
		} 
	}
	private static ArrayList<ArrayList<Short>> GET_ADDRESS_OCTETS(short oct1, boolean o1, short oct2, boolean o2, short oct3, boolean o3, short oct4, boolean o4) {
		PL.con(oct1 + ", " + o1 + ", " + oct2 + ", " + o2 + ", " + oct3 + ", " + o3 + ", " +oct4 + ", " + o4);
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
			count*=ADDRESS.get(n).size()-1;
		}
		
		PL.con("Generated " + count + " addresses.");
		
		return ADDRESS;
		
	}
	
	
	
	/**Akarshit Wal
	//https://stackoverflow.com/a/30691451/6580858
	 * 
	 * Modified to include x
	**/
	public static boolean validate(final String ip) {
	    String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
	    return ip.matches(PATTERN);
	}
}
