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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class user_add_project extends AppCompatActivity {
EditText e1,e2,e3;
Button b1;
FloatingActionButton f1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_project);
        e1=findViewById(R.id.editTextTextPersonName7);
        e2=findViewById(R.id.editTextTextPersonName8);
        e3=findViewById(R.id.editTextTextPersonName9);
        f1=findViewById(R.id.floatingActionButton3);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), user_view_project.class);
                startActivity(i);
            }
        });
        b1=findViewById(R.id.button6);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                String project = e1.getText().toString();
                String details = e2.getText().toString();
                String amount = e3.getText().toString();
                if (project.length() == 0) {
                    e1.setError("");
                } else if (details.length() == 0) {
                    e2.setError("");
                } else if (amount.length() == 0) {
                    e3.setError("");
                } else {
                    SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ip", "");
                    String url = sh.getString("url", "") + "/user_add_project";

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
                            }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            params.put("login_id", sh.getString("lid", ""));//passing to python
                            params.put("project", project);//passing to python
                            params.put("details", details);//passing to python
                            params.put("amount", amount);//passing to python

//                        params.put("userid", user_view_other_user.userid);//passing to python
//                        params.put("mysk", user_view_skill_cate.mysk);//passing to python
                            //passing to python


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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), userhome.class));
    }
}