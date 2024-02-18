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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class user_make_payment_1 extends AppCompatActivity {
    EditText e1,e2,e5,e4;
    Button b1;
   TextView e3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_make_payment_1);
        e1=findViewById(R.id.amt);
        e2=findViewById(R.id.det);
        e3=findViewById(R.id.textView4);
        e4=findViewById(R.id.ed);
        e5=findViewById(R.id.nas);
        e3.setText(user_requested_project.amount);
        b1=findViewById(R.id.button10);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardnumber=e1.getText().toString();
                String cvv=e2.getText().toString();
                String amount=e3.getText().toString();
                String date=e4.getText().toString();
                String name=e5.getText().toString();
                if (cardnumber.length()!=16)
                {
                    e1.setError("Enter Card Number");
                }
                else if(cvv.length()!=3)
                {
                    e2.setError("Enter CVV");
                }
                else if(amount.length()==0)
                {
                    e3.setError("");
                }
                else if(date.length()==0)
                {
                    e4.setError("Enter Date");
                }
                else if(name.length()==0)
                {
                    e5.setError("Enter Your Name");
                }
                else {
                    SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ip", "");
                    String url = sh.getString("url", "") + "/user_make_payment";

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
                            params.put("amount", amount);//passing to python
                            params.put("pre_id", user_requested_project.preq_id);//passing to python


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