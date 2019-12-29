package com.zehret.ipportscannerX.gui;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class NetScanTools {
	
	//Return the address that lie on the same subnet as the current machine.. equivalent to the following
	//			...Machine is on 192.168.1.30 == 192.168.1.x -> 192.168.1.0-255
	public static ArrayList<String> generateSubnetAddress() {
		try {
			InetAddress ipAddr = InetAddress.getLocalHost();
			System.out.println("Machine IP: " + ipAddr);
			return null;
		//	return generateAddress(ip.split("\\.")[0],ip.split("\\.")[1],ip.split("\\.")[2],"%");
		}catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}catch(IOException e) {
			//Device offline
			System.out.println("!! Device is not connected to a network.");
			return null;
		}
	}
	public static ArrayList<String> generateAddress(String address) {
		return generateAddress(address.split("\\.")[0],address.split("\\.")[1],address.split("\\.")[2],address.split("\\.")[3]);
	}
	
	//Return an ArrayList with all of the addresses that will be scanned. each block will contain a number or a wild card.
	//Support for ranges in each block is an option for the future, as well as comma separated blocks i.e. 1,2.1.1.1 will scan 1.1.1.1 and 2.1.1.1
	public static ArrayList<String> generateAddress(String block_1, String block_2, String block_3, String block_4) {
		ArrayList<String> _addr = new ArrayList<String>();
		
		short b1 = -1, b2 = -1, b3 = -1, b4 = -1; //blocks 1-4
		boolean wcb1 = false, wcb2 = false, wcb3 = false, wcb4 = false; //if blocks 1-4 are wild card
		
		//Process block_1
		if(block_1.equals("x")) {
			//wild card - add all addresses
			wcb1 = true;
		} else {
			try {
				b1 = Short.parseShort(block_1);
			} catch(NumberFormatException e) {
				System.out.println("!! Invalid format in block 1 \"" + block_1 + "\"");
			}
		}
		
		
		//Process block_2
		if(block_2.equals("x")) {
			//wild card - add all addresses
			wcb2 = true;
		} else {
			try {
				b2 = Short.parseShort(block_2);
			} catch(NumberFormatException e) {
				System.out.println("!! Invalid format in block 2 \"" + block_2 + "\"");
			}
		}
		
		
		//Process block_3
		if(block_3.equals("x")) {
			//wild card - add all addresses
			wcb3 = true;
		} else {
			try {
				b3 = Short.parseShort(block_3);
			} catch(NumberFormatException e) {
				System.out.println("!! Invalid format in block 3 \"" + block_3 + "\"");
			}
		}
		
		
		//Process block_4
		if(block_4.equals("x")) {
			//wild card - add all addresses
			wcb4 = true;
		} else {
			try {
				b4 = Short.parseShort(block_4);
			} catch(NumberFormatException e) {
				System.out.println("!! Invalid format in block 4 \"" + block_4 + "\"");
			}
		}
		
		//2 dimensional ArrayList. Each row is a block (always qty 4), each column is a subsequent block following the one before it in the address.
		ArrayList<ArrayList<Short>> _rawAddresses = new ArrayList<ArrayList<Short>>();
		
		if(wcb1) {
			_rawAddresses.add(0, new ArrayList<Short>());
			for(short i = 0; i < 256; i++) {
				_rawAddresses.get(0).add(i);
			}
		} else {
			_rawAddresses.add(0, new ArrayList<Short>());
			_rawAddresses.get(0).add(b1);
		}
		
		if(wcb2) {
			_rawAddresses.add(1, new ArrayList<Short>());
			for(short i = 0; i < 256; i++) {
				_rawAddresses.get(1).add(i);
			}
		} else {
			_rawAddresses.add(1, new ArrayList<Short>());
			_rawAddresses.get(1).add(b2);
		}
		
		if(wcb3) {
			_rawAddresses.add(2, new ArrayList<Short>());
			for(short i = 0; i < 256; i++) {
				_rawAddresses.get(2).add(i);
			}
		} else {
			_rawAddresses.add(2, new ArrayList<Short>());
			_rawAddresses.get(2).add(b3);
		}
		
		if(wcb4) {
			_rawAddresses.add(3, new ArrayList<Short>());
			for(short i = 0; i < 256; i++) {
				_rawAddresses.get(3).add(i);
			}
		} else {
			_rawAddresses.add(3, new ArrayList<Short>());
			_rawAddresses.get(3).add(b4);
		}
		System.out.println(_rawAddresses.size());
		_addr = transformAddressList(_rawAddresses);
		System.out.println(_addr.size());
				
		return _addr;		
	}
	
	public static ArrayList<Integer> generatePorts(String ports) {
		ArrayList<Integer> portRange = new ArrayList<Integer>();
		
		if(ports.trim().contains("x")) {
			return generatePorts("0-65535");
		}
		
		String[] portsAndRanges = ports.split(",");
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
		return portRange;
	}
		
	//Convert the two dimensional ArrayList of short types to a one dimensional ArrayList with String types of full addresses.
	private static ArrayList<String> transformAddressList(ArrayList<ArrayList<Short>> addresses) {
		ArrayList<String> _addr = new ArrayList<String>();
		for(int n1 = 0; n1 < addresses.get(0).size(); n1++) {
			for(int n2 = 0; n2 < addresses.get(1).size(); n2++) {
				for(int n3 = 0; n3 < addresses.get(2).size(); n3++) {
					for(int n4 = 0; n4 < addresses.get(3).size(); n4++) {
						_addr.add(new String(addresses.get(0).get(n1) + "." + addresses.get(1).get(n2) + "." + addresses.get(2).get(n3) + "." + addresses.get(3).get(n4)));
					}
				}
			}
		}
		return _addr;
	}
	
	public static boolean validateIPV4(String IPV4) {
		String[] sub = IPV4.split("\\.");
		try {
			if(!sub[0].equals("x")) {
				short b1 = Short.parseShort(sub[0]);
				if((b1 < 1)||(b1 > 255)) return false;
			}
			
			if(!sub[1].equals("x")) {
				short b2 = Short.parseShort(sub[1]);
				if((b2 < 0)||(b2 > 255)) return false;
			}
			
			if(!sub[2].equals("x")) {
				short b3 = Short.parseShort(sub[2]);
				if((b3 < 0)||(b3 > 255)) return false;
			}
			
			if(!sub[3].equals("x")) {
				short b4 = Short.parseShort(sub[3]);
				if((b4 < 0)||(b4 > 255)) return false;
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	public static boolean validatePorts(String portList) {
		String[] sub = portList.split(",");
		try {			
			for(int i = 0; i < sub.length; i++) {
				if(sub[i].contains("-")) {
					int bound1 = Integer.parseInt(sub[i].split("-")[0]);
					int bound2 = Integer.parseInt(sub[i].split("-")[1]);
					if(bound2 <= bound1) {
						return false;
					}
				} else {
					int port = Integer.parseInt(sub[i]);
					if((port < 1)||(port > 65535)) {
						return false;
					}
				}
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}

}
