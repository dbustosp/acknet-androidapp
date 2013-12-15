package org.twodee.acknet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cs491.acknet.R;

public class ChangelanguageActivity extends Activity implements
OnItemSelectedListener{
	
	// Spinner element
    Spinner spinner;
    Locale myLocale;
    int check = 0;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelanguage);
        
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        
        // Spinner Drop down elements
        List<String> languages = new ArrayList<String>();
        languages.add("Japanese");
        languages.add("English");
        languages.add("Spanish");
        languages.add("Portuguese");
        
        
     // Creating adapter for spinner
     ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
     // Drop down layout style - list view with radio button
     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
     // attaching data adapter to spinner
  	 spinner.setAdapter(dataAdapter);		
        
	}

	
	
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// On selecting a spinner item
		System.out.println("Item seleccionado");
		check = check + 1;
		if(check > 1){
			System.out.println("Item seleccionado: " + position);
			//String item = parent.getItemAtPosition(position).toString();
			if(position == 1){
				System.out.println("Clicking English");
				setLocale("en");
			}else if (position == 2){
				System.out.println("Clicking Spanish");
				setLocale("es");
			}
		}
	}

	
    public void setLocale(String lang) {
    	 
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        
        Intent refresh = new Intent(this, DashboardActivity.class);
        startActivity(refresh);
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

}
