package org.twodee.acknet;

public final class Connection {
	
	// Variables
	private String IP;
	
	private static Connection instance = new Connection();
	
	
	private Connection(){
		IP = "http://137.28.229.205:3000";	
	}
	
	public static Connection getInstance(){
		return instance;
	}
	
	public String getIp(){
		return IP;
	}
}
