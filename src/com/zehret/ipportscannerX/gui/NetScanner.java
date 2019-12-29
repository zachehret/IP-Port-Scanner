package com.zehret.ipportscannerX.gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class NetScanner {
	private static boolean offline = false;
	public static int TIMEOUT = 75;
	public static ArrayList<ScanResult> SCAN(ArrayList<String> addresses, ArrayList<Integer> ports) throws NoRouteToHostException {
		PortScannerGUI.PSG.scanningProgressBar.setIndeterminate(true);
		float scansCompleted = 0f;
		float scans = (float)addresses.size() * (float)ports.size();
		System.out.println(scans + " scan(s) to complete.");
		ArrayList<ScanResult> results = new ArrayList<ScanResult>();
		for(int i = 0; i < addresses.size(); i++) {
			results.add(new ScanResult(addresses.get(i)));
			for(int j = 0; j < ports.size(); j++) {
				if(offline) return null; //IF the device is offline, exit the scan method.
				
				if(testConnection(addresses.get(i), ports.get(j))) {
					System.out.println("\tFound connection @ " + addresses.get(i) + ":" + ports.get(j));
					results.get(results.size()-1).addFoundPort(ports.get(j));
				} else {
					//System.out.println("\tNo connection found @ " + addresses.get(i) + ":" + ports.get(j));
					results.get(results.size()-1).addNotFoundPort(ports.get(j));
				}
				scansCompleted++;
				PortScannerGUI.PSG.progressBar.setValue((int) (Math.floor((scansCompleted/scans)*100f)));
			}
		}
		PortScannerGUI.PSG.progressBar.setValue(100);
		PortScannerGUI.PSG.scanningProgressBar.setIndeterminate(false);
		return results;
	}
	
	private static boolean testConnection(String addr, int port) throws NoRouteToHostException {
		try {
			Socket _sock = new Socket();
			_sock.setReuseAddress(true);
			SocketAddress sa = new InetSocketAddress(addr, port);
			_sock.connect(sa, TIMEOUT);
			_sock.close();
			return true;			
		}catch(NoRouteToHostException e) {
			//DEVICE OFFLINE
			System.out.println("!! Device is not connected to a network.");
			offline = true;
			throw new NoRouteToHostException("Device is not connected to a network.");
		}catch(IOException e) {
			return false;
		}
	}

}
