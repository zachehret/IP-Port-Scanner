package com.zehret.ipportscanner;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;

import org.zehret.console.Console;
import org.zehret.console.util.ConsoleConfiguration;
import org.zehret.console.util.PL;

public class PortScanner {

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
		while(true) {
			while(true) {

				ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.yellow;
				ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = ConsoleConfiguration.BACKGROUND_COLOR;
				PL.con("########## MAIN ##########", PL.NONEC);
				PL.con("# [1] Select Address.....#", PL.NONE);
				PL.con("# [2] All Addresses......#", PL.NONE);
				PL.con("# [3] Modify Settings....#", PL.NONE);
				ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.ORANGE;
				ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = ConsoleConfiguration.BACKGROUND_COLOR;
				PL.con("# [4] Generate Report ...#", PL.NONEC);
				PL.con("# [5] Exit...............#", PL.NONE);
				ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.yellow;
				ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = ConsoleConfiguration.BACKGROUND_COLOR;
				PL.con("##########################", PL.NONEC);
				String input = Console.prompt("Option", Console.SHOWINPUTOPTION	, PL.NONE, false);
				ArrayList<Integer> ports = new ArrayList<Integer>();
				if(input.equalsIgnoreCase("1")) {
					String address;
					while(true) {
						try {
							address = Console.prompt("Address:", Console.SHOWINPUTOPTION, PL.NONE, PL.NO_ALERT);
							String copy = address;
							if(validate(address.replaceAll("x", "0"))) {
								address = copy;
								break;
							}
							throw new IllegalArgumentException();
						}catch(IllegalArgumentException e) {
							ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.orange;
							PL.con("Invalid address",PL.NONEC);
						}
					}
					ports = promptForPortSelection();
					
					
					PL.con_encap("THIS PROCESS WILL SCAN " + address + " FOR THE DESIRED PORTS", '#', PL.NONE, false);
					
					while(true) {
						input = Console.prompt("Start process? (Y/N)", Console.SHOWINPUTOPTION, PL.NONE, true);
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
								
								PL.con("Generating addresses..",PL.NONE);
								addresses = GET_ADDRESS_OCTETS(oct1, o1, oct2, o2, oct3, o3, oct4, o4);
	
								String duration;
								long scanCount =  getAddressCount(addresses) * ports.size();
								System.out.println(scanCount + " scans will be compelted.");
								long msToComplete = scanCount * timeoutMS;
								duration = convertMStoHHMMSS(msToComplete);
								input = Console.prompt("This process is estimated to take about " + duration + ". Begin process? (Y/N)", Console.SHOWINPUTOPTION, PL.NONE, true);
								if(input.equalsIgnoreCase("Y")) {
									scan(addresses,ports);
									
									PL.con("Done.",PL.NONE);
									Console.pause(1000);
									break;	
								}
								else if(input.equalsIgnoreCase("N")) {
									break;
								} else {
									continue;
								}
						} else if(input.equalsIgnoreCase("N")) {
							break;
						} else {
							continue;
						}
					}
				} else if(input.equalsIgnoreCase("2")) { 
					ports = promptForPortSelection();
					PL.con_encap("THIS PROCESS WILL SCAN ALL IP ADDRESS FOR DESIRED PORTS", '#', PL.NONE, false);
					while(true) {
						ArrayList<ArrayList<Short>> addresses = GET_ADDRESS_OCTETS((short)-1,true,(short)-1,true,(short)-1,true,(short)-1,true); //equal to x.x.x.x as input for the ips to scan
						String duration;
						long scanCount =  getAddressCount(addresses) * ports.size();
						System.out.println(scanCount + " scans will be compelted.");
						long msToComplete = scanCount * timeoutMS;
						duration = convertMStoHHMMSS(msToComplete);
						input = Console.prompt("This process is estimated to take about " + duration + ". Begin process? (Y/N)", Console.SHOWINPUTOPTION, PL.NONE, true);
						if(input.equalsIgnoreCase("Y")) {
							scan(addresses,ports);
							break;
						} else if(input.equalsIgnoreCase("N")) {
							break;
						} else {
							continue;
						}
					}
				} else if(input.equalsIgnoreCase("3")) {
					boolean showMenu = true;
					while(true) {
						if(showMenu) {
							PL.con("###############################", PL.NONE);
							PL.con("# [1] ... Modify Timeout Time #", PL.NONE);
							PL.con("# [2] ... Modify Report Mode  #", PL.NONE);
							PL.con("# [3] ... Exit                #", PL.NONE);
							PL.con("###############################", PL.NONE);
							showMenu = false;
						}
						try {	
							String settingsPrompt = Console.prompt("Option: ", Console.SHOWINPUTOPTION, PL.NONE, PL.NO_ALERT);
							if(settingsPrompt.equalsIgnoreCase("1")) {
								
								timeoutMS = Integer.parseInt(promptForSetting("Modify timeout time (Current value: " + timeoutMS + ")","Time in milliseconds. Default value: 500ms"));
								showMenu = true;
							} else if(settingsPrompt.equalsIgnoreCase("2")) {
								promptForReportMode();
								showMenu = true;
							} else if(settingsPrompt.equalsIgnoreCase("3")) {
								Console.pause(1000);
								break;
							} else {
								ConsoleConfiguration.MSG_CUSTOM_COLOR = ConsoleConfiguration.TEXT_COLOR;
								ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = Color.decode("#990000");
								PL.con("Invalid menu selection",PL.NONEC,PL.ALERT);
								continue;
							}
							PL.con("# Setting Updated #", PL.NONE);
							Console.pause(1000);
						}catch(Exception e) { }
					}
				} else if(input.equalsIgnoreCase("4")) {
					try {
						generateReport(DATA, transformAddressList(addresses), ports);
					}catch(NullPointerException e) {
						ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.yellow;
						ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = Color.decode("#990000");
						PL.con("# INCOMPLETE OR CORRUPT REPORT DATA #", PL.NONEC, PL.ALERT);
						Console.pause(5000);
					}
				} else if(input.equalsIgnoreCase("5")) {
					System.exit(0);
				} else {
					continue;
				}
			}
		}	
	}
	private static String convertMStoHHMMSS(long ms) {
		long hh, mm, ss;
		ss = (long)(ms/ 1000) % 60;
		mm = (long)(ms / (1000 * 60)) % 60;
		hh = (long)(ms / (1000 * 60 * 60)) % 24;
		if(hh > 0) {
			return new String(padL0(hh) + " hours");
		} else if(mm > 0) {
			return new String(padL0(mm) + " minutes");
		} else {
			return new String(padL0(ss) + " seconds");
		}
	}
	private static String padL0(long s) {
		if(s < 10)
			return "0" + s;
		return s+"";
	}
	private static ArrayList<String> transformAddressList(ArrayList<ArrayList<Short>> addresses) {
		ArrayList<String> addStrings = new ArrayList<String>();
		for(int n1 = 0; n1 < addresses.get(0).size(); n1++) {
			for(int n2 = 0; n2 < addresses.get(1).size(); n2++) {
				for(int n3 = 0; n3 < addresses.get(2).size(); n3++) {
					for(int n4 = 0; n4 < addresses.get(3).size(); n4++) {
						addStrings.add(new String(addresses.get(0).get(n1) + "." + addresses.get(1).get(n2) + "." + addresses.get(2).get(n3) + "." + addresses.get(3).get(n4)));
					}
				}
			}
		}
		return addStrings;
	}
	private static long getAddressCount(ArrayList<ArrayList<Short>> adrs) {
		long count = 0;
		for(int i = 0; i < adrs.size(); i++) {
			count+=adrs.get(i).size();
		}
		return count;
	}
	private static void scan(ArrayList<ArrayList<Short>> addresses, ArrayList<Integer> ports) {
		Console.setupProgressPopup(0, false);
		PL.con("");
		Console.consoleWindow.setLastOutputFieldText("");
		for(int n1 = 0; n1 < addresses.get(0).size(); n1++) {
			for(int n2 = 0; n2 < addresses.get(1).size(); n2++) {
				for(int n3 = 0; n3 < addresses.get(2).size(); n3++) {
					for(int n4 = 0; n4 < addresses.get(3).size(); n4++) {
						for(int n = 0; n < ports.size(); n++) {
							testConnection(addresses.get(0).get(n1),addresses.get(1).get(n2),addresses.get(2).get(n3),addresses.get(3).get(n4),ports.get(n));
							//System.out.println(n1 + ">>" + addresses.get(0).size() + ", " + n2 + ">>" + addresses.get(1).size() + ", " + n3 + ">>" + addresses.get(2).size() + ", " + n4 + ">>" + addresses.get(3).size() + "," + n + ">>" + ports.size());
							Console.setProgress(((float)n/ports.size())*100);
						}
					}
				}
			}
		}
		Console.closeProgressPopup();
	}
	private static String promptForSetting(String message, String tip) {
		
		PL.con_encap(message + "(" + tip + ")", '#', PL.NONE, PL.NO_ALERT);
		
		return Console.prompt("Set value", Console.SHOWINPUTOPTION, PL.NONE, PL.NO_ALERT);
	}
	
	private static void promptForReportMode() {
		
		ConsoleConfiguration.AUTO_RESET_CUSTOM_COLOR = false;
		ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.LIGHT_GRAY;
		ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = ConsoleConfiguration.BACKGROUND_COLOR;
		PL.con("###############################", PL.NONE);
		PL.con("# [1] ... Sort by Address     #", PL.NONEC);
		PL.con("# [2] ... Sort by Port (DFLT) #", PL.NONE);
		PL.con("# [3] ... Address Summary     #", PL.NONEC);
		PL.con("# [4] ... Port Summary        #", PL.NONEC);
		PL.con("# [5] ... Exit (No change)    #", PL.NONE);
		PL.con("###############################", PL.NONE);
		while(true) {
			try {
				String reportPrompt = Console.prompt("Set value: ", Console.SHOWINPUTOPTION);
				int option = Integer.parseInt(reportPrompt);
				if(option == 5) {
					break;
				} else if (option > 5) {
					throw new NumberFormatException("Value > 5");
				} else if((option == 1)||(option == 3)||(option == 4)) {
					ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.yellow;
					ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = ConsoleConfiguration.BACKGROUND_COLOR;
					PL.con("Selected report option not yet supported.",PL.NONEC);
					throw new IllegalArgumentException("Unsupported report type");
				}
				reportOption = option;
				break;
			} catch(NumberFormatException e) {
				ConsoleConfiguration.MSG_CUSTOM_COLOR = ConsoleConfiguration.TEXT_COLOR;
				ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = Color.decode("#990000");
				PL.con("Invalid Data",PL.NONEC);
			} catch(Exception e) {
				
			}
				
		}
		ConsoleConfiguration.AUTO_RESET_CUSTOM_COLOR = true;
	}
	private static ArrayList<Integer> promptForPortSelection() {
		while(true) {
			try {
				String ports = Console.prompt("Enter the ports to scan (Separate values and ranges by commas, use x to scan all ports. E.g. 3,22-80,3000.)", Console.SHOWINPUTOPTION, PL.NONE, PL.NO_ALERT);
				ports.replaceAll("\\s","");
				if((ports.startsWith("x"))||(ports.startsWith("X"))) {
					return parsePortSelection("0-65535");
				}
				return parsePortSelection(ports);
			}catch(Exception e) {
				PL.con("Invalid Data", PL.NONE);
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
		  Console.consoleWindow.setLastOutputFieldText(" \tScanning Port " + port + " at address " + address);
		  s.connect(sa, timeoutMS);
		  Console.consoleWindow.setLastOutputFieldText("Received response >> " + address + ":" + port + " <<");
		  PL.con("");
		  Console.consoleWindow.setLastOutputFieldText("");
		  s.close(); 
		  DATA.ADD_PORT(address, port);
		}catch (NoRouteToHostException e) {
			while(true) {
				ConsoleConfiguration.MSG_CUSTOM_COLOR = Color.yellow;
				ConsoleConfiguration.BACKGROUND_CUSTOM_COLOR = Color.red;
				PL.con("Device Offline.",PL.NONEC);
				String retry = Console.prompt("Retry? (Y/N)", Console.SHOWINPUTOPTION, PL.NONEC, true);
				if(retry.equalsIgnoreCase("Y")) {
					PL.con("");
					Console.consoleWindow.setLastOutputFieldText("");
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
			DATA.ADD_ADDRESS(address);
			//e.printStackTrace();
			return;         
		} 
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
	
	
	public static String generateReport(ReportData rd, ArrayList<String> addresses, ArrayList<Integer> ports) {
		String report = new String("");
		PL.con("Generating Report..", PL.NONE);
		
		if(reportOption == 2) {
			report += createEncapsulatedReportHeaderFooter("Scan Results by Port " + new Date().toString(), 59, '#');
			//report += createEncapsulatedReportLine("" + "\n",59,'#');
			//report += createEncapsulatedReportLine("" + "\n",59,'#');
			String portsNotFound = "";
			
			for(int i = 0; i < ports.size(); i++) {
				boolean needsFoundPortsHeader = true;
				boolean needsNotFoundPortsHeader = true;
				for(int n = 0; n < rd.getAddressDataCount(); n++) {
					if(rd.getDataAtIndex(n).hasPortConnection(ports.get(i))) {
						if(needsFoundPortsHeader) {
							needsFoundPortsHeader = false;
							report += createEncapsulatedReportSection("PORT " + ports.get(i), 59, '#') + "\n";
						}
						report += createEncapsulatedReportLine(rd.getDataAtIndex(n).getAddress() + " Y", 59, '#') + "\n";
					} else {
						if(needsNotFoundPortsHeader) {
							needsNotFoundPortsHeader = false;
							portsNotFound += createEncapsulatedReportSection("PORT " + ports.get(i), 59, '#') + "\n";
						}
						portsNotFound += createEncapsulatedReportLine(rd.getDataAtIndex(n).getAddress() + " N", 59, '#') + "\n";
					}
				}
			}
			report += portsNotFound + createEncapsulatedReportHeaderFooter("Scan Results by Port " + new Date().toString(), 59, '#');
			
		}
		
		PL.con("Report Generated", PL.NONE);
		PL.con("\n\n" + report, PL.NONE);
		DATA.DUMP();
		return report;
	}
	
	private static String createEncapsulatedReportSection(String data, int length, char encap) {
		return (createEncapsulationChunk(encap,((length - data.length())/2)-1) + " " + data + " " + createEncapsulationChunk(encap, length)).substring(0,length);
	}
	private static String createEncapsulatedReportLine(String data, int length, char encap) {
		if(length < data.length())
			throw new IllegalArgumentException("DATA LENGTH CANNOT BE LESS THAN DESIRED LINE LENGTH");
		String line = encap + " " + data;
		if(line.length() < length) {
			for(int n = line.length(); n < length - 1; n++) {
				line+=" ";
			}
			line+=encap;
		} else {
			line = line.substring(0, length);
		}
		return line;
	}
	
	private static String createEncapsulatedReportHeaderFooter(String data, int length, char encap) {
		
		String footerHeader = new String("");
		footerHeader = footerHeader.concat(createEncapsulationChunk(encap, length) + "\n");		
		
		String preDatChunk = createEncapsulationChunk(encap,((length - data.length())/2)-1);
		
		footerHeader = footerHeader.concat( (preDatChunk + " " + data + " " + preDatChunk + preDatChunk).substring(0,length) + "\n");
		
		footerHeader = footerHeader.concat(createEncapsulationChunk(encap, length) + "\n");
		
		return footerHeader;
		
	}
	
	private static String createEncapsulationChunk(char encap, int length) {
		String chunk = new String();
		for(int n = 0; n < length; n++) {
			chunk+=encap;
		}
		return chunk;
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
//Used to store report data
class ReportData {
	ArrayList<AddressData> DATA;
	public ReportData() {
		DATA = new ArrayList<AddressData>();
	}
	
	public void DUMP() {
		while(DATA.size() > 0) {
			DATA.remove(0);
		}
	}

	public void printAddresses() {
		for(int n = 0; n < DATA.size(); n++) {
			PL.con(DATA.get(n).getAddress(), PL.NONE);
		}
		
	}

	public int ADD_ADDRESS(String address) {
		return getAddressIndex(address);
	}
	public void ADD_PORT(String address, int port) {
		DATA.get(getAddressIndex(address)).ADD_PORT_CONNECTION(port);
	}

	protected int getAddressIndex(String address) {
		for(int n = DATA.size() - 1; n >= 0; n--) {
			if(address.equals(DATA.get(n).getAddress())) {
				return n;
			}
		}
		DATA.add(new AddressData(address));
		return getAddressIndex(address);
	}
	
	protected AddressData getDataAtIndex(int i) {
		return DATA.get(i);
	}
	
	protected int getAddressDataCount() {
		return DATA.size();
	}
}
//Used to store data for each address within the report data.
class AddressData {
	private final String address;
	protected ArrayList<Integer> ports;
	public AddressData(String address) {
		this.address = address;
		this.ports = new ArrayList<Integer>();
	}
	
	public boolean hasPortConnection(int port) {
		for(int n = 0; n < ports.size(); n++) {
			if(ports.get(n) == port) {
				return true;
			}
		}
		return false;
	}

	public void ADD_PORT_CONNECTION(int port) {
		this.ports.add(port);
	}
	public String getAddress() {
		return address;
	}
	public int findPortAtIndex(int i) {
		return ports.get(i);
	}
	public int getPortCount() {
		return ports.size();
	}
	
	
}