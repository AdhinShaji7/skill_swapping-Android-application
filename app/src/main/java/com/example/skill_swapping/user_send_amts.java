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

public class user_send_amts extends AppCompatActivity {
EditText e1,e2;
Button b1;
String amtss,dets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_send_amts);
        e1 = (EditText) findViewById(R.id.amt);
        e2 = (EditText) findViewById(R.id.det);
        b1 = (Button) findViewById(R.id.snd_amt);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amtss = e1.getText().toString();
                dets = e2.getText().toString();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url = sh.getString("url", "") + "/user_send_amts";
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
                                        startActivity(new Intent(getApplicationContext(), user_view_request.class));
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
                        params.put("amtss", amtss);//passing to python
                        params.put("detss", dets);//passing to python
                        params.put("req_id", user_view_request.req_ids);//passing to python

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
        });



    }
}