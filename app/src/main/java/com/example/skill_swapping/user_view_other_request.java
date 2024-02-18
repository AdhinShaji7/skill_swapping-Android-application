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

public class user_view_other_request extends AppCompatActivity {
    ListView l1;
    SharedPreferences sh;
    String[] pro_name,date,value,status,uid,pid,prid;
    public  static String req,proid,statss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_other_request);
        l1=findViewById(R.id.listview);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ip","");
        String url=sh.getString("url","")+"/view_other_request";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                prid=new String[js.length()];
                                pro_name = new String[js.length()];
                                pid = new String[js.length()];
                                uid=new String[js.length()];

                                date = new String[js.length()];
                                status = new String[js.length()];
                                value = new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    pid[i] = u.getString("project_id");
                                    prid[i] = u.getString("prequest_id");

                                    pro_name[i] = u.getString("projects");
                                    uid[i] = u.getString("user_id");

                                    date[i] = u.getString("date");//dbcolumn name in double quotes
                                    status[i] = u.getString("stat");

                                    value[i] = "\nproject name : " + pro_name[i] + "\ndate : " + date[i]+ "\nstatus : " + status[i];
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
                params.put("login_id", sh.getString("lid", ""));//passing to python



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
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                proid = pid[i];
                req = prid[i];
                statss = status[i];
                if (statss.equalsIgnoreCase("pending")) {
                    final CharSequence[] items1 = {"Accept", "Reject", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(user_view_other_request.this);
                    builder.setItems(items1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items1[item].equals("Accept")) {

                                String url = "http://" + sh.getString("ip", "") + "/api/user_accept_req";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Toast.makeText(getApplicationContext(), " Accept", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Accept failed", Toast.LENGTH_SHORT).show();
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
                                        params.put("pro_id", user_view_other_request.proid);//passing to python
                                        params.put("req_id", user_view_other_request.req);//passing to python
                                        params.put("login_id", sh.getString("lid", ""));//passing to python


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
                            } else if (items1[item].equals("Reject")) {

                                String url = "http://" + sh.getString("ip", "") + "/api/user_reject_req";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Toast.makeText(getApplicationContext(), " Reject", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "reject failed", Toast.LENGTH_SHORT).show();
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
                                        params.put("pro_id", user_view_other_request.proid);//passing to python
//                                        params.put("pid", sh.getString("pid",""));//passing to python
                                        params.put("req_id", user_view_other_request.req);//passing to python
                                        params.put("login_id", sh.getString("lid", ""));//passing to python


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

//


                            }

                        }
                    });
                    builder.show();
                } else {

                    final CharSequence[] items1 = {"View Payment", "View Issue", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(user_view_other_request.this);
                    builder.setItems(items1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items1[item].equals("Accept")) {

                                String url = "http://" + sh.getString("ip", "") + "/api/user_accept_req";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Toast.makeText(getApplicationContext(), " Accept", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Accept failed", Toast.LENGTH_SHORT).show();
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
                                        params.put("pro_id", user_view_other_request.proid);//passing to python
//                                        params.put("pid", sh.getString("pid",""));//passing to python
                                        params.put("req_id", user_view_other_request.req);//passing to python
                                        params.put("login_id", sh.getString("lid", ""));//passing to python


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




                            } else if (items1[item].equals("Cancel")) {
                                dialog.dismiss();
                            } else if (items1[item].equals("Reject")) {

                                String url = "http://" + sh.getString("ip", "") + "/api/user_reject_req";
                                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                        new Response.Listener<NetworkResponse>() {
                                            @Override
                                            public void onResponse(NetworkResponse response) {
                                                try {


                                                    JSONObject obj = new JSONObject(new String(response.data));

                                                    if (obj.getString("status").equals("ok")) {

                                                        Toast.makeText(getApplicationContext(), " Reject", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getApplicationContext(), userhome.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "reject failed", Toast.LENGTH_SHORT).show();
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
                                        params.put("pro_id", user_view_other_request.proid);//passing to python
//                                        params.put("pid", sh.getString("pid",""));//passing to python
                                        params.put("req_id", user_view_other_request.req);//passing to python
                                        params.put("login_id", sh.getString("lid", ""));//passing to python


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

//


                            } else if (items1[item].equals("View Payment")) {
                                Intent i = new Intent(getApplicationContext(), user_view_payment.class);
                                startActivity(i);
                            } else if (items1[item].equals("View Issue")) {
                                Intent i = new Intent(getApplicationContext(), user_view_issue.class);
                                startActivity(i);
                            }
                        }
                    });
                    builder.show();
                }
            }


        });
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), userhome.class));
    }
}