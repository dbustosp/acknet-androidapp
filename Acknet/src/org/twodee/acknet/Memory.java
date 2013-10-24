package org.twodee.acknet;

import org.json.JSONException;
import org.json.JSONObject;

public class Memory {

	private String username;
	private String password;
	
	public Memory(String username, String password){
		super();
		this.username = username;
		this.password = password;
	}
	
	public JSONObject toJSON() {
		
	    JSONObject object = new JSONObject();
	    try {
	      object.put("username", username);
	      object.put("password", password);
	    } catch (JSONException e) {
	      e.printStackTrace();
	    }
	    return object;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
