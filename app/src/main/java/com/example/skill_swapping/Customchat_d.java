
package com.example.skill_swapping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;


public class Customchat_d extends ArrayAdapter<String>
{ 
	//needs to extend ArrayAdapter

	private String[] message;         //for custom view name item
	private String[] date;
	private String[] s_id;	//for custom view photo items
	private Activity context;       //for to get current activity context
	SharedPreferences sh;
	public Customchat_d(Activity context, String[] message, String[] s_id, String[] date)
	{
		//constructor of this class to get the values from main_activity_class

		super(context, R.layout.customlist_chat_d, message);
		this.context = (Activity) context;
		this.message = message;
		this.date= date;
		this.s_id= s_id;
	}

	@SuppressLint("ResourceAsColor") @Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		//override getView() method

		LayoutInflater inflater = context.getLayoutInflater();
		View listViewItem = inflater.inflate(R.layout.customlist_chat_d, null, true);
		//cust_list_view is xml file of layout created in step no.2

		TextView t1= (TextView) listViewItem.findViewById(R.id.textView1);
		TextView t2 = (TextView) listViewItem.findViewById(R.id.textView2);

		if(s_id[position].equalsIgnoreCase(login.lid))
		{
			//t2.setText(message[position]+"\n["+date[position]+"]");
			t2.setText(message[position]);
			t2.setBackgroundColor(Color.parseColor("#82ccdd"));

		}
		else
		{
			//t1.setText(message[position]+"\n["+date[position]+"]");
			t1.setText(message[position]);
			t1.setBackgroundColor(Color.parseColor("#78e08f"));
			
		}
		sh=PreferenceManager.getDefaultSharedPreferences(getContext());
		return  listViewItem;
	}
}
