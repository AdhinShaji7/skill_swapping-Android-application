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

public class user_view_highlight extends AppCompatActivity {
ListView l1;
String [] highlights,photo,oid;

SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_highlight);
        l1=findViewById(R.id.listview);
        l1=(ListView) findViewById(R.id.listview);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        sh.getString("ip","");
        String url=sh.getString("url","")+"/user_view_highlight";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>(){
                    @Override
                    public void onResponse(NetworkResponse response) {


                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                highlights = new String[js.length()];

                                oid = new String[js.length()];

                                photo = new String[js.length()];
//                                val=new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    highlights[i] = u.getString("highlights");//dbcolumn name in double quotes
                                    oid[i] = u.getString("project_id");
                                    photo[i] = u.getString("image");




                                }
                                l1.setAdapter(new custom_image(getApplicationContext(),highlights,photo,oid));//custom_view_service.xml and li is the listview object


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
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                params.put("login_id", sh.getString("login_id",""));//passing to python

                params.put("pro_id",user_view_other_project.oth_pid);//passing to python


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



                final CharSequence[] items1 = {"Send Request"};

                AlertDialog.Builder builder = new AlertDialog.Builder(user_view_highlight.this);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        Intent i = new Intent(getApplicationContext(),userhome .class);

                        startActivity(i);
                        String url = "http://" + sh.getString("ip", "") + "/api/prorequest";

                        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse response) {
                                        try {


                                            JSONObject obj = new JSONObject(new String(response.data));

                                            if (obj.getString("status").equals("ok")) {

                                                Toast.makeText(getApplicationContext(), " success", Toast.LENGTH_SHORT).show();
//
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
                                }){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                params.put("login_id", sh.getString("lid",""));
                                params.put("pro_id",user_view_other_project.oth_pid);
                                params.put("user_id",user_view_other_project.userid);


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
                builder.show();

            }
        });
    }

}