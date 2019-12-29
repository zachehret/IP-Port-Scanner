package com.zehret.ipportscannerX.gui;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import java.awt.Font;
import java.awt.Label;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.border.CompoundBorder;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.beans.PropertyChangeListener;
import java.net.NoRouteToHostException;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JProgressBar;

public class PortScannerGUI extends JFrame {
	private static ArrayList<String> _ADDR = new ArrayList<String>();
	private static ArrayList<Integer> _PORT = new ArrayList<Integer>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 797438356315140587L;
	protected JTextField ipAddressTextField;
	protected JTextField portsTextField;
	
	protected static PortScannerGUI PSG;
	protected JCheckBox chckbxHttp;
	protected JCheckBox chckbxHttps;
	protected JButton btnBeginScanbasic;
	protected JCheckBox chckbxDns;
	protected JCheckBox chckbxFtp;
	protected JCheckBox chckbxSftp;
	protected JCheckBox chckbxFtp_control;
	protected JCheckBox chckbxLocalSubnet;
	protected JCheckBox chckbxLocalHost;
	protected JTextPane txtpnPortSelection;
	protected JTextPane txtpnAddresses;
	protected JCheckBox chckbxImap;
	protected JCheckBox chckbxSql;
	protected JButton beginAdvancedScanButton;
	protected JProgressBar progressBar;
	protected JProgressBar scanningProgressBar;
	
	public PortScannerGUI() {
		//650x530
		this.setSize(650, 530);
		this.setMaximumSize(new Dimension(650, 570));
		this.setMinimumSize(new Dimension(650, 570));
		setResizable(false);
		setVisible(true);
		setEnabled(true);
		setTitle("Network Port Scanner GUIx V1.00");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{30, 135, 0, 30, 0};
		gridBagLayout.rowHeights = new int[]{30, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 15, 30, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 2.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JTextPane txtpnBasic = new JTextPane();
		txtpnBasic.setText("Basic");
		txtpnBasic.setFont(new Font("Dialog", Font.BOLD, 20));
		txtpnBasic.setEditable(false);
		GridBagConstraints gbc_txtpnBasic = new GridBagConstraints();
		gbc_txtpnBasic.gridwidth = 2;
		gbc_txtpnBasic.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnBasic.fill = GridBagConstraints.VERTICAL;
		gbc_txtpnBasic.gridx = 1;
		gbc_txtpnBasic.gridy = 1;
		getContentPane().add(txtpnBasic, gbc_txtpnBasic);
		
		txtpnAddresses = new JTextPane();
		txtpnAddresses.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnAddresses.setText("Address Selection");
		txtpnAddresses.setEditable(false);
		GridBagConstraints gbc_txtpnAddresses = new GridBagConstraints();
		gbc_txtpnAddresses.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnAddresses.fill = GridBagConstraints.BOTH;
		gbc_txtpnAddresses.gridx = 1;
		gbc_txtpnAddresses.gridy = 2;
		getContentPane().add(txtpnAddresses, gbc_txtpnAddresses);
		
		txtpnPortSelection = new JTextPane();
		txtpnPortSelection.setText("Port Selection");
		txtpnPortSelection.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnPortSelection.setEditable(false);
		GridBagConstraints gbc_txtpnPortSelection = new GridBagConstraints();
		gbc_txtpnPortSelection.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnPortSelection.fill = GridBagConstraints.BOTH;
		gbc_txtpnPortSelection.gridx = 2;
		gbc_txtpnPortSelection.gridy = 2;
		getContentPane().add(txtpnPortSelection, gbc_txtpnPortSelection);
		
		JPanel addressSelectionPanel = new JPanel();
		addressSelectionPanel.setBorder(new CompoundBorder());
		GridBagConstraints gbc_addressSelectionPanel = new GridBagConstraints();
		gbc_addressSelectionPanel.insets = new Insets(0, 0, 5, 5);
		gbc_addressSelectionPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_addressSelectionPanel.gridx = 1;
		gbc_addressSelectionPanel.gridy = 3;
		getContentPane().add(addressSelectionPanel, gbc_addressSelectionPanel);
		GridBagLayout gbl_addressSelectionPanel = new GridBagLayout();
		gbl_addressSelectionPanel.columnWidths = new int[]{0, 0};
		gbl_addressSelectionPanel.rowHeights = new int[]{0, 0, 0};
		gbl_addressSelectionPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_addressSelectionPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		addressSelectionPanel.setLayout(gbl_addressSelectionPanel);
		
		chckbxLocalHost = new JCheckBox("Local Host");
		GridBagConstraints gbc_chckbxLocalHost = new GridBagConstraints();
		gbc_chckbxLocalHost.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxLocalHost.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxLocalHost.gridx = 0;
		gbc_chckbxLocalHost.gridy = 0;
		addressSelectionPanel.add(chckbxLocalHost, gbc_chckbxLocalHost);
		
		chckbxLocalSubnet = new JCheckBox("Local Subnet");
		GridBagConstraints gbc_chckbxLocalSubnet = new GridBagConstraints();
		gbc_chckbxLocalSubnet.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxLocalSubnet.gridx = 0;
		gbc_chckbxLocalSubnet.gridy = 1;
		addressSelectionPanel.add(chckbxLocalSubnet, gbc_chckbxLocalSubnet);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 3;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		chckbxFtp_control = new JCheckBox("20 - FTP");
		GridBagConstraints gbc_chckbxFtp_control = new GridBagConstraints();
		gbc_chckbxFtp_control.anchor = GridBagConstraints.WEST;
		gbc_chckbxFtp_control.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxFtp_control.gridx = 0;
		gbc_chckbxFtp_control.gridy = 0;
		panel.add(chckbxFtp_control, gbc_chckbxFtp_control);
		
		chckbxSftp = new JCheckBox("115 - SFTP");
		GridBagConstraints gbc_chckbxSftp = new GridBagConstraints();
		gbc_chckbxSftp.anchor = GridBagConstraints.WEST;
		gbc_chckbxSftp.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxSftp.gridx = 1;
		gbc_chckbxSftp.gridy = 0;
		panel.add(chckbxSftp, gbc_chckbxSftp);
		
		chckbxFtp = new JCheckBox("21 - FTP");
		GridBagConstraints gbc_chckbxFtp = new GridBagConstraints();
		gbc_chckbxFtp.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxFtp.anchor = GridBagConstraints.WEST;
		gbc_chckbxFtp.gridx = 0;
		gbc_chckbxFtp.gridy = 1;
		panel.add(chckbxFtp, gbc_chckbxFtp);
		
		chckbxImap = new JCheckBox("143 - IMAP");
		GridBagConstraints gbc_chckbxImap = new GridBagConstraints();
		gbc_chckbxImap.anchor = GridBagConstraints.WEST;
		gbc_chckbxImap.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxImap.gridx = 1;
		gbc_chckbxImap.gridy = 1;
		panel.add(chckbxImap, gbc_chckbxImap);
		
		chckbxDns = new JCheckBox("53 - DNS");
		GridBagConstraints gbc_chckbxDns = new GridBagConstraints();
		gbc_chckbxDns.anchor = GridBagConstraints.WEST;
		gbc_chckbxDns.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDns.gridx = 0;
		gbc_chckbxDns.gridy = 2;
		panel.add(chckbxDns, gbc_chckbxDns);
		
		chckbxSql = new JCheckBox("156 - SQL Server");
		GridBagConstraints gbc_chckbxSql = new GridBagConstraints();
		gbc_chckbxSql.anchor = GridBagConstraints.WEST;
		gbc_chckbxSql.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxSql.gridx = 1;
		gbc_chckbxSql.gridy = 2;
		panel.add(chckbxSql, gbc_chckbxSql);
		
		chckbxHttp = new JCheckBox("80 - HTTP");
		GridBagConstraints gbc_chckbxHttp = new GridBagConstraints();
		gbc_chckbxHttp.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxHttp.anchor = GridBagConstraints.WEST;
		gbc_chckbxHttp.gridx = 0;
		gbc_chckbxHttp.gridy = 3;
		panel.add(chckbxHttp, gbc_chckbxHttp);
		
		chckbxHttps = new JCheckBox("443 - HTTPS");
		GridBagConstraints gbc_chckbxHttps = new GridBagConstraints();
		gbc_chckbxHttps.anchor = GridBagConstraints.WEST;
		gbc_chckbxHttps.gridx = 1;
		gbc_chckbxHttps.gridy = 3;
		panel.add(chckbxHttps, gbc_chckbxHttps);
		
		btnBeginScanbasic = new JButton("Begin Scan (Basic)");
		btnBeginScanbasic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnBeginScanbasic.isEnabled()) {
					Thread t = new Thread() {
						public void run() {
							try {
								btnBeginScanbasic.setEnabled(false);
								beginAdvancedScanButton.setEnabled(false);
								ipAddressTextField.setEditable(false);
								portsTextField.setEditable(false);
								
								//BASIC SCAN SUBMIT
								System.out.println("Basic scan initiated...");
								System.out.println("Compiling address list based on selections.");
								ArrayList<String> addr = new ArrayList<String>();
								if(PSG.chckbxLocalHost.isSelected()) {
									addr.add("127.0.0.1");
								}
								if(PSG.chckbxLocalSubnet.isSelected()) {
									addr.addAll(NetScanTools.generateSubnetAddress());
								}
								System.out.println("Done.");
								System.out.println("Compiling ports list based on selections.");
								ArrayList<Integer> ports = compilePortsList_Basic();
								System.out.println("Done.");
								System.out.println("Beginning Scan!");
								NetScanner.SCAN(addr, ports);
								System.out.println("Done.");
							}catch(NoRouteToHostException e) {
								JOptionPane.showMessageDialog(PSG, "Device is offline. Please connect to a network and try again.", "Device Offline", JOptionPane.ERROR_MESSAGE);
							}catch(Exception e) {
								JOptionPane.showMessageDialog(PSG, "An error ocurred while scanning. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
							}
							
							btnBeginScanbasic.setEnabled(true);
							beginAdvancedScanButton.setEnabled(true);
							ipAddressTextField.setEditable(true);
							portsTextField.setEditable(true);
						}
					};
					t.start();
				}				
			}
		});
		GridBagConstraints gbc_btnBeginScanbasic = new GridBagConstraints();
		gbc_btnBeginScanbasic.gridwidth = 2;
		gbc_btnBeginScanbasic.insets = new Insets(0, 0, 5, 5);
		gbc_btnBeginScanbasic.gridx = 1;
		gbc_btnBeginScanbasic.gridy = 5;
		getContentPane().add(btnBeginScanbasic, gbc_btnBeginScanbasic);
		
		JTextPane txtpnAdvanced = new JTextPane();
		txtpnAdvanced.setFont(new Font("Dialog", Font.BOLD, 20));
		txtpnAdvanced.setText("Advanced");
		txtpnAdvanced.setEditable(false);
		GridBagConstraints gbc_txtpnAdvanced = new GridBagConstraints();
		gbc_txtpnAdvanced.fill = GridBagConstraints.VERTICAL;
		gbc_txtpnAdvanced.gridwidth = 2;
		gbc_txtpnAdvanced.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnAdvanced.gridx = 1;
		gbc_txtpnAdvanced.gridy = 7;
		getContentPane().add(txtpnAdvanced, gbc_txtpnAdvanced);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.fill = GridBagConstraints.VERTICAL;
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 1;
		gbc_horizontalStrut.gridy = 8;
		getContentPane().add(horizontalStrut, gbc_horizontalStrut);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		GridBagConstraints gbc_lblIpAddress = new GridBagConstraints();
		gbc_lblIpAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpAddress.anchor = GridBagConstraints.EAST;
		gbc_lblIpAddress.gridx = 1;
		gbc_lblIpAddress.gridy = 9;
		getContentPane().add(lblIpAddress, gbc_lblIpAddress);
		
		ipAddressTextField = new JTextField();
		ipAddressTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(PSG.ipAddressTextField.getText().length() > 14) {
					PSG.ipAddressTextField.setText(PSG.ipAddressTextField.getText().substring(0,14));
				}
				if(!NetScanTools.validateIPV4(PSG.ipAddressTextField.getText())) {
					PSG.ipAddressTextField.setForeground(Color.white);
					PSG.ipAddressTextField.setBackground(Color.decode("#8b0000"));
				} else {
					PSG.ipAddressTextField.setForeground(Color.black);
					PSG.ipAddressTextField.setBackground(Color.decode("#00ff00"));
				}
			}
		});
		GridBagConstraints gbc_ipAddressTextField = new GridBagConstraints();
		gbc_ipAddressTextField.insets = new Insets(0, 0, 5, 5);
		gbc_ipAddressTextField.fill = GridBagConstraints.BOTH;
		gbc_ipAddressTextField.gridx = 2;
		gbc_ipAddressTextField.gridy = 9;
		getContentPane().add(ipAddressTextField, gbc_ipAddressTextField);
		ipAddressTextField.setColumns(15);
		
		JTextPane txtrEnterIpAddress = new JTextPane();
		txtrEnterIpAddress.setEditable(false);
		txtrEnterIpAddress.setText("Enter IP address in the format xxx.xxx.xxx.xxx; Use x as wildcard");
		GridBagConstraints gbc_txtrEnterIpAddress = new GridBagConstraints();
		gbc_txtrEnterIpAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtrEnterIpAddress.gridwidth = 2;
		gbc_txtrEnterIpAddress.insets = new Insets(0, 0, 5, 5);
		gbc_txtrEnterIpAddress.gridx = 1;
		gbc_txtrEnterIpAddress.gridy = 10;
		getContentPane().add(txtrEnterIpAddress, gbc_txtrEnterIpAddress);
		
		JLabel lblPorts = new JLabel("Ports");
		GridBagConstraints gbc_lblPorts = new GridBagConstraints();
		gbc_lblPorts.anchor = GridBagConstraints.EAST;
		gbc_lblPorts.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorts.gridx = 1;
		gbc_lblPorts.gridy = 11;
		getContentPane().add(lblPorts, gbc_lblPorts);
		
		portsTextField = new JTextField();
		portsTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(!NetScanTools.validatePorts(PSG.portsTextField.getText())) {
					PSG.portsTextField.setForeground(Color.white);
					PSG.portsTextField.setBackground(Color.decode("#8b0000"));
				} else {
					PSG.portsTextField.setForeground(Color.black);
					PSG.portsTextField.setBackground(Color.decode("#00ff00"));
				}
			}
		});
		portsTextField.setColumns(15);
		GridBagConstraints gbc_portsTextField = new GridBagConstraints();
		gbc_portsTextField.insets = new Insets(0, 0, 5, 5);
		gbc_portsTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_portsTextField.gridx = 2;
		gbc_portsTextField.gridy = 11;
		getContentPane().add(portsTextField, gbc_portsTextField);
		
		JTextPane txtpnEnterPortsIn = new JTextPane();
		txtpnEnterPortsIn.setEditable(false);
		txtpnEnterPortsIn.setText("Enter ports in a range from 1 to 65535. Separate ports with a comma.");
		GridBagConstraints gbc_txtpnEnterPortsIn = new GridBagConstraints();
		gbc_txtpnEnterPortsIn.gridwidth = 2;
		gbc_txtpnEnterPortsIn.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnEnterPortsIn.fill = GridBagConstraints.BOTH;
		gbc_txtpnEnterPortsIn.gridx = 1;
		gbc_txtpnEnterPortsIn.gridy = 12;
		getContentPane().add(txtpnEnterPortsIn, gbc_txtpnEnterPortsIn);
		
		beginAdvancedScanButton = new JButton("Begin Scan (Advanced)");
		beginAdvancedScanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(beginAdvancedScanButton.isEnabled()) {
					Thread t = new Thread() {
						public void run() {
							try {
								btnBeginScanbasic.setEnabled(false);
								beginAdvancedScanButton.setEnabled(false);
								ipAddressTextField.setEditable(false);
								portsTextField.setEditable(false);
								
								//ADVANCED SCAN SUBMIT
								System.out.println("Advanced scan initiated...");
								System.out.println("Compiling address list based on input.");
								ArrayList<String> addr = new ArrayList<String>();
								addr.addAll(NetScanTools.generateAddress(ipAddressTextField.getText()));
								System.out.println("Done.");
								System.out.println("Compiling ports list based on selections.");
								ArrayList<Integer> ports = new ArrayList<Integer>();
								ports.addAll(NetScanTools.generatePorts(portsTextField.getText()));
								System.out.println("Done.");
								System.out.println("Beginning Scan!");
								NetScanner.SCAN(addr, ports);
								System.out.println("Done.");
							}catch(NoRouteToHostException e) {
								JOptionPane.showMessageDialog(PSG, "Device is offline. Please connect to a network and try again.", "Device Offline", JOptionPane.ERROR_MESSAGE);
							}catch(Exception e) {
								JOptionPane.showMessageDialog(PSG, "An error ocurred while scanning. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
							}
							
							btnBeginScanbasic.setEnabled(true);
							beginAdvancedScanButton.setEnabled(true);
							ipAddressTextField.setEditable(true);
							portsTextField.setEditable(true);
						}
					};
					t.start();
				}
			}
		});
		GridBagConstraints gbc_beginAdvancedScanButton = new GridBagConstraints();
		gbc_beginAdvancedScanButton.gridwidth = 2;
		gbc_beginAdvancedScanButton.insets = new Insets(0, 0, 5, 5);
		gbc_beginAdvancedScanButton.gridx = 1;
		gbc_beginAdvancedScanButton.gridy = 13;
		getContentPane().add(beginAdvancedScanButton, gbc_beginAdvancedScanButton);
		
		scanningProgressBar = new JProgressBar();
		scanningProgressBar.setForeground(new Color(50, 205, 50));
		GridBagConstraints gbc_scanningProgressBar = new GridBagConstraints();
		gbc_scanningProgressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_scanningProgressBar.gridwidth = 2;
		gbc_scanningProgressBar.insets = new Insets(0, 0, 5, 5);
		gbc_scanningProgressBar.gridx = 1;
		gbc_scanningProgressBar.gridy = 15;
		getContentPane().add(scanningProgressBar, gbc_scanningProgressBar);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setStringPainted(true);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 16;
		getContentPane().add(progressBar, gbc_progressBar);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.validate();
	}
	
	public static void main(String[] args) {
		System.out.println("Starting Network Port Scanner GtxtpnBasicUIx V1.00");
		PSG = new PortScannerGUI();
	}
	
	private ArrayList<Integer> compilePortsList_Basic() {
		ArrayList<Integer> _ports = new ArrayList<Integer>();
		if(PSG.chckbxFtp_control.isSelected()) {
			_ports.add(20);
		}
		if(PSG.chckbxFtp.isSelected()) {
			_ports.add(21);
		}
		if(PSG.chckbxDns.isSelected()) {
			_ports.add(53);
		}
		if(PSG.chckbxHttp.isSelected()) {
			_ports.add(80);
		}
		if(PSG.chckbxSftp.isSelected()) {
			_ports.add(115);
		}
		if(PSG.chckbxImap.isSelected()) {
			_ports.add(143);
		}
		if(PSG.chckbxSql.isSelected()) {
			_ports.add(156);
		}
		if(PSG.chckbxHttps.isSelected()) {
			_ports.add(443);
		}
		
		return _ports; 
	}
	private ArrayList<Integer> compilePortsList_Adv() {
		ArrayList<Integer> _ports = new ArrayList<Integer>();
		
		return _ports; 
	}


}
