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

public class user_make_payment extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5;
    Button b1;
    String  cno,cvv,ed,namess,amt;
SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_make_payment);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText) findViewById(R.id.cno);
        e2=(EditText) findViewById(R.id.cvv);
        e3=(EditText) findViewById(R.id.ed);
        e4=(EditText) findViewById(R.id.nas);
        e5=(EditText) findViewById(R.id.amt);
        e5.setText(user_sended_request.amts);//displaying amount in a textbox
        e5.setEnabled(false);
        b1=(Button) findViewById(R.id.btn_pay);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cno = e1.getText().toString();
                cvv = e2.getText().toString();
                ed = e3.getText().toString();
                namess = e4.getText().toString();
                amt = e5.getText().toString();
                if (cno.equalsIgnoreCase("")) {
                    e1.setError("");
                    e1.setFocusable(true);
                } else if (cvv.equalsIgnoreCase("")) {
                    e2.setError("");
                    e2.setFocusable(true);
                } else if (ed.equalsIgnoreCase("")) {
                    e3.setError("");
                    e3.setFocusable(true);
                } else if (namess.equalsIgnoreCase("")) {
                    e4.setError("");
                    e4.setFocusable(true);
                } else if (amt.equalsIgnoreCase("")) {
                    e5.setError("");
                    e5.setFocusable(true);
                } else {

                    String url = sh.getString("url", "") + "/user_make_payment";
                    Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    try {
//                                    pd.dismiss();


                                        JSONObject obj = new JSONObject(new String(response.data));

                                        if (obj.getString("status").equals("ok")) {
                                            Toast.makeText(getApplicationContext(), " success", Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor ed = sh.edit();

//                                        ed.putString("lid", obj.getString("lid"));
//                                        ed.commit();
//                                        Intent i = new Intent(getApplicationContext(), user_add_category.class);
//                                        startActivity(i);
                                            startActivity(new Intent(getApplicationContext(), userhome.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(), " failed", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            params.put("amts", user_sended_request.amts);//passing to python
                            params.put("req_id", user_sended_request.req_ids);//passing to python

                            return params;
                        }


                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            long imagename = System.currentTimeMillis();
                            //  params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
                }

            }
        });
        }



    }

