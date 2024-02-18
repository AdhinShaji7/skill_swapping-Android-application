package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class user_send_complaint extends AppCompatActivity {
EditText e1;
Button b1;
ListView l1;
String[] complaint,reply,value,c_id,uid,date;
public  static String user_id;
SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_complaint);
        e1=findViewById(R.id.editTextTextPersonName11);
        b1=findViewById(R.id.button8);
        l1=findViewById(R.id.listview);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ip","");
        String url = "http://" + sh.getString("ip", "") + "/api/user_view_comp";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                c_id = new String[js.length()];
                                uid=new String[js.length()];
                                complaint = new String[js.length()];
                                reply = new String[js.length()];
                                date = new String[js.length()];
                                value = new String[js.length()];

                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    c_id[i] = u.getString("complaint_id");
                                    uid[i] = u.getString("user_id");
                                    date[i] = u.getString("date");
                                    complaint[i] = u.getString("complaint");//dbcolumn name in double quotes
                                    reply[i] = u.getString("reply");

                                    value[i] = "\ncomplaint : " + complaint[i] + "\nReply : " + reply[i]+ "\nDate : " + date[i];
                                }

                                l1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
//                                l2.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
//                                ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, value);
//                                l2.setAdapter(aa);
                            } else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("login_id", sh.getString("lid", ""));//passing to python
                params.put("user_id", user_send_complaint.user_id);//passing to python

                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //   params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comp=e1.getText().toString();

                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sh.getString("ip","");
                String url=sh.getString("url","")+"/user_send_comp";

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {


                                    JSONObject obj = new JSONObject(new String(response.data));

                                    if (obj.getString("status").equals("ok")) {

                                        Toast.makeText(getApplicationContext(), " success", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        params.put("login_id", sh.getString("lid",""));//passing to python
                           params.put("user_id", user_send_complaint.user_id);//passing to python
                        params.put("comp", comp);//passing to python



                        return params;
                    }
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        //  long imagename = System.currentTimeMillis();
                        //     params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                        return params;
                    }
                };

                Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);

            }
        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), userhome.class));
    }
}