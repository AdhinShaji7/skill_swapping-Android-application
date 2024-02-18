package com.example.skill_swapping;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class custom_image extends BaseAdapter {
    private Context context;

    String [] highlights,photo,pid;

    public custom_image(Context con, String[]highlights,String[]photo,String[] pid)

    {
        this.pid=pid;
        this.context=con;

        this.highlights=highlights;

        this.photo=photo;




    }
    @Override
    public int getCount() {
        return highlights.length  ;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            gridView=inflator.inflate(R.layout.activity_custom_image,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView labourname1=(TextView)gridView.findViewById(R.id.na);















//        ImageView im=(ImageView)gridView.findViewById(R.id.imageView4);

        labourname1.setTextColor(Color.BLACK);
//        labourname2.setTextColor(Color.BLACK);
//        labourname3.setTextColor(Color.BLACK);
//        fphone.setTextColor(Color.BLACK);
//        femail.setTextColor(Color.BLACK);
//        edistrict.setTextColor(Color.BLACK);




        labourname1.setText("Name :"+highlights[i]);
//        labourname2.setText("\nGender : "+gender[i]);
//        labourname3.setText("\n\nPlace : "+place[i]);
//        fphone.setText("\n\n\nPhone : "+phone[i]);
//        femail.setText("\n\n\n\nEmail : "+email[i]);
//        edistrict.setText("\n\n\n\n\nDistrict : "+district[i]);



        ImageView im=(ImageView) gridView.findViewById(R.id.imgss);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String ip=sh.getString("ip","");
        String url="http://"+ ip +"/"+photo[i];

        Log.d("+++",url);

        Picasso.with(context).load(url).into(im);

        return gridView;
    }
}


