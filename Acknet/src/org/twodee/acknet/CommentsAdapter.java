package org.twodee.acknet;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cs491.acknet.R;

public class CommentsAdapter extends BaseAdapter{
	
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public CommentsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }


	public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row_comment, null);

        TextView body = (TextView)vi.findViewById(R.id.body); 
        TextView username = (TextView)vi.findViewById(R.id.username); 
        TextView date = (TextView)vi.findViewById(R.id.date_comment); 
                
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        body.setText(song.get(TimelineActivity.KEY_BODY));
        date.setText(song.get(TimelineActivity.KEY_DATE));
        username.setText(song.get(TimelineActivity.KEY_USERNAME));
        return vi;
    }
}
