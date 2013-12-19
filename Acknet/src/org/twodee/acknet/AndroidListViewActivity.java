package org.twodee.acknet;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.cs491.acknet.R;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class AndroidListViewActivity extends ListActivity {
	    String imgPath;
	    ImageView imgUser;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        // storing string resources into Array
        String[] option_settings = getResources().getStringArray(R.array.options_settings);
         
        // Binding resources Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, option_settings));
         
        ListView lv = getListView();
 
        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
	              int position, long id) {
	               
	              switch(position){
	              	case 0: chooseLanguage(); break;
	              }
              }
        });
    }
	
	public void chooseLanguage(){
		Intent myIntent = new Intent(getApplicationContext(), ChangelanguageActivity.class);
    	startActivityForResult(myIntent, 0);			
	}
}