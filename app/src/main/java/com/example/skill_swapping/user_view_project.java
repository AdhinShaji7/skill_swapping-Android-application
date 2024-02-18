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

public class user_view_project extends AppCompatActivity {
    ListView l1;
    SharedPreferences sh;
    String[]  project,details,date,amount,status,uid,proid,value;
    public  static String pro_id,statss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_project);
        l1=(ListView) findViewById(R.id.listview);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        sh.getString("ip","");

        String url=sh.getString("url","")+"/user_view_project";
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
                                proid = new String[js.length()];
                                project = new String[js.length()];
                                details = new String[js.length()];
                                date = new String[js.length()];
                                amount = new String[js.length()];
                                status = new String[js.length()];

                                value = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    uid[i] = u.getString("user_id");
                                    proid[i] = u.getString("project_id");
                                    project[i] = u.getString("projects");//dbcolumn name in double quotes
                                    details[i] = u.getString("details");
                                    date[i] = u.getString("date");//dbcolumn name in double quotes
                                    amount[i] = u.getString("amount");
                                    status[i] = u.getString("status");


                                    value[i] = "\nProject Name : " + project[i] + "\nDetails : " + details[i]+ "\nDate : " + date[i]+ "\nAmount : " + amount[i]+ "\nStatus : " + status[i];
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
                pro_id = proid[i];
                statss = status[i];
                if (statss.equalsIgnoreCase("Available")) {
                    final CharSequence[] items1 = {"Add Highlights", "Project Unavailable","Delete", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(user_view_project.this);
                    builder.setItems(items1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items1[item].equals("Add Highlights")) {

                                Intent i = new Intent(getApplicationContext(), user_add_highlight.class);
                                startActivity(i);




                            } else if (items1[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                            else if (items1[item].equals("Delete")) {
                                String url = "http://" + sh.getString("ip", "") + "/api/deleteproject";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), " deleted", Toast.LENGTH_SHORT).show();

                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
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
                                        params.put("pro_id", user_view_project.pro_id);//passing to python
                                        params.put("login_id", sh.getString("lid",""));//passing to python

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

                            }else if (items1[item].equals("Project Unavailable")) {
//
                                String url = "http://" + sh.getString("ip", "") + "/api/pro_unavailable";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Toast.makeText(getApplicationContext(), " Updated", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), user_view_project.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
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
                                        params.put("pro_id", user_view_other_project.oth_pid);//passing to python
                                        params.put("project_id", user_view_project.pro_id);//passing to python


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
                    builder.show();
                }
                else {
                    final CharSequence[] items1 = {"Available","Delete", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(user_view_project.this);
                    builder.setItems(items1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items1[item].equals("Available")) {

                                String url = "http://" + sh.getString("ip", "") + "/api/pro_available";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Toast.makeText(getApplicationContext(), " Available", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), user_view_project.class);
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
                                        params.put("project_id", user_view_project.pro_id);//passing to python
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


                            }
                            else if (items1[item].equals("Delete")) {
                                String url = "http://" + sh.getString("ip", "") + "/api/deleteproject";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), " deleted", Toast.LENGTH_SHORT).show();

                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
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
                                        params.put("pro_id", user_view_project.pro_id);//passing to python
                                        params.put("login_id", sh.getString("lid",""));//passing to python

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

                            }else if (items1[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
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