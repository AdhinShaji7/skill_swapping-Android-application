package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class user_view_other_user extends AppCompatActivity {
    ListView l1;
    SharedPreferences sh;
    public  static String userid;
    //ArrayList  name,gender,place,phone,photo,email,district,val;
    String[]  fname,lname,place,phone,email,uid,value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_other_user);
        l1=(ListView) findViewById(R.id.listview);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        sh.getString("ip","");

        String url=sh.getString("url","")+"/user_view_other_user";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {

                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
//                                Toast.makeText(getApplicationContext(), "evida und", Toast.LENGTH_LONG).show();
                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                uid = new String[js.length()];
                                fname = new String[js.length()];
                                lname = new String[js.length()];
                                place = new String[js.length()];
                                phone = new String[js.length()];
                                email = new String[js.length()];

                                value = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    uid[i] = u.getString("user_id");
                                    fname[i] = u.getString("ufname");//dbcolumn name in double quotes
                                    lname[i] = u.getString("ulname");
                                    place[i] = u.getString("place");//dbcolumn name in double quotes
                                    phone[i] = u.getString("phone");
                                    email[i] = u.getString("email");


                                    value[i] = "\nFirst Name : " + fname[i] + "\nLast Name : " + lname[i]+ "\nPlace : " + place[i]+ "\nPhone : " + phone[i]+ "\nEmail : " + email[i];
                                }

                                l1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));

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
                params.put("login_id", sh.getString("lid",""));//passing to python
                params.put("user_id", sh.getString("uid",""));//passing to python


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

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userid=uid[i];
                final CharSequence[] items1 = {"Badge or Achive"};

                AlertDialog.Builder builder = new AlertDialog.Builder(user_view_other_user.this);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items1[item].equals("Badge or Achive")) {

                            String url = "http://" + sh.getString("ip", "") + "/api/viewskillandcate";
                            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                    new Response.Listener<NetworkResponse>() {
                                        @Override
                                        public void onResponse(NetworkResponse response) {
                                            try {


                                                JSONObject obj = new JSONObject(new String(response.data));

                                                if (obj.getString("status").equals("ok")) {

                                                    Intent i = new Intent(getApplicationContext(), user_view_skill_cate.class);
                                                    startActivity(i);
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
                                        public void onErrorResponse(VolleyError volleyError) {

                                        }
                                    }) {

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    params.put("pid", userid);//passing to python
//                                        params.put("pid", sh.getString("pid",""));//passing to python


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

//                                if (items1[item].equals("Delete"))


                        } else if (items1[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }


        });
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), userhome.class));
    }
}