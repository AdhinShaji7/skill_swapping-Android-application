package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class login extends AppCompatActivity {
    EditText e1,e2;
    Button b1;
    TextView b2;
    SharedPreferences sh;
    public static String lid;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = (EditText) findViewById(R.id.uname);
        e2 = (EditText) findViewById(R.id.pwd);
        b1 = (Button) findViewById(R.id.button2);
        b2=(TextView) findViewById(R.id.button3) ;
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),user_register.class));
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url=sh.getString("url","")+"/logins";
                Toast.makeText(getApplicationContext(),"jjjjjjjjjjjjjj"+url,Toast.LENGTH_LONG).show();
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {
//                                    pd.dismiss();


                                    JSONObject obj = new JSONObject(new String(response.data));

                                    if(obj.getString("status").equals("ok")){
                                        Toast.makeText(getApplicationContext(), " success", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor ed=sh.edit();
                                        lid=obj.getString("lid");
                                        ed.putString("lid", obj.getString("lid"));
                                        ed.commit();
                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Registration failed" ,Toast.LENGTH_SHORT).show();
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
                        params.put("username", e1.getText().toString());//passing to python
                        params.put("password", e2.getText().toString());//passing to python
//                        params.put("phon", phone);
//                        params.put("pla", place);
//                        params.put("pos", post);
//                        params.put("pin", pin);
//                        params.put("p", p);
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