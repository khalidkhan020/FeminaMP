package com.linchpin.periodtracker.adpators;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomeCursorAdapter extends SimpleCursorAdapter
{
	private int mSelectedPosition;
	Cursor items;
	private Context context;
	private int layout;
	@SuppressWarnings("deprecation")
	public CustomeCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
	{
		super(context, layout, c, from, to);
		 this.context = context;
		    this.layout = layout;
	}
	
	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2)
	{
		/*int nameCol = c.getColumnIndex("phrase");
	    String name = c.getString(nameCol);


	    TextView name_text = (TextView) v.findViewById(R.id.phrase);
	    if (name_text != null) {
	        name_text.setText(name);
	    }

	    //name_text.setTextColor(Color.GREEN);

	    int position = c.getPosition(); 
	    if (mSelectedPosition == position) {
	       v.setBackgroundResource(R.drawable.listviewbackground);
	       v.getBackground().setDither(true);
	    } else {
	       v.setBackgroundColor(Color.BLACK);
	    }*/
	}
	
	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup parent)
	{
		Cursor c = getCursor();

	    final LayoutInflater inflater = LayoutInflater.from(context);
	    View v = inflater.inflate(layout, parent, false);

	   /* int nameCol = c.getColumnIndex("phrase");
	    String name = c.getString(nameCol);
*/
	   
	    return v;
	}
	
}
