package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class user_register extends AppCompatActivity {
EditText e1,e2,e3,e4,e5,e6,e7;
Button b1;
SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        e1=findViewById(R.id.editTextTextPersonName);
        e2=findViewById(R.id.editTextTextPersonName2);
        e3=findViewById(R.id.editTextTextPersonName3);
        e4=findViewById(R.id.editTextPhone);
        e5=findViewById(R.id.editTextTextEmailAddress);
        e6=findViewById(R.id.editTextTextPersonName5);
        e7=findViewById(R.id.editTextTextPassword);
        b1=findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=e1.getText() .toString();
                String lname= e2.getText() .toString();
                String place=e3.getText() .toString();
                String phone= e4.getText() .toString();
                String email=e5.getText() .toString();
                String uname=e6.getText().toString();
                String pwd=e7.getText().toString();
                if (e1.length()==0)
                {
                    e1.setError("Enter First Name");
                }
                else if(e2.length()==0)
                {
                    e2.setError("Enter Last Name");
                }
                else if(e3.length()==0)
                {
                    e3.setError("Enter Place Name");
                }
                else if(e4.length()!=10)
                {
                    e4.setError("Enter Phone number");
                }
                else if(e5.length()==0)
                {
                    e5.setError("Enter Email");
                }
                else if(e6.length()==0)
                {
                    e6.setError("Enter User Name");
                }
                else if(e7.length()==0)
                {
                    e7.setError("Enter Password");
                }
                else {
                    SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ip", "");
                    String url = sh.getString("url", "") + "/user_reg";

                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    try {


                                        JSONObject obj = new JSONObject(new String(response.data));

                                        if (obj.getString("status").equals("ok")) {

                                            Toast.makeText(getApplicationContext(), " success", Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor ed = sh.edit();

                                            Intent i = new Intent(getApplicationContext(), login.class);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
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
                            params.put("user_id", sh.getString("user_id", ""));//passing to python

                            params.put("fname", fname);//passing to python
                            params.put("lname", lname);//passing to python
                            params.put("place", place);//passing to python
                            params.put("phone", phone);//passing to python
                            params.put("email", email);//passing to python

                            params.put("uname", uname);//passing to python
                            params.put("pwd", pwd);//passing to python


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

            }
        });


    }
}