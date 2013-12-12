package org.twodee.acknet;

import java.util.ArrayList;

public class Notifications {
	public static ArrayList<Notification> notifications;
	
	private Notifications() {
		notifications = new ArrayList<Notification>();
	}
	
	private static Notifications instance;
	
	public static Notifications getInstance() {
	    if (instance == null){
	    	instance = new Notifications();
	    	notifications = new ArrayList<Notification>();
	    }
	    return instance;
	}
}
