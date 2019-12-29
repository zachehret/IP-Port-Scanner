package com.zehret.ipportscannerX.gui;

import java.util.ArrayList;

public class ScanResult {
	protected final String address;
	protected ArrayList<Integer> foundPorts = new ArrayList<Integer>();
	protected ArrayList<Integer> notFoundPorts = new ArrayList<Integer>();
	public ScanResult(String addr) {
		this.address = addr;		
	}
	public void addFoundPort(int port) {
		this.foundPorts.add(port);
	}
	public void addNotFoundPort(int port) {
		this.notFoundPorts.add(port);
	}
	
}
