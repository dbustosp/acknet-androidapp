package org.twodee.acknet;

public final class Connection {
	
	// Variables
	private String IP;
	
	private static Connection instance = new Connection();
	
	
	private Connection(){
		IP = "http://arcane-crag-4782.herokuapp.com";	
	}
	
	public static Connection getInstance(){
		return instance;
	}
	
	public String getIp(){
		return IP;
	}
}
