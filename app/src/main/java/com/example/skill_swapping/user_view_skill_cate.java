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

public class user_view_skill_cate extends AppCompatActivity {
    ListView ll;
    String[] myskill_id,cname,sname,val;
    public  static String mysk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_skill_cate);
        ll=(ListView) findViewById(R.id.listview);


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url=sh.getString("url","")+"/user_view_skill_cate";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>(){
                    @Override
                    public void onResponse(NetworkResponse response) {


                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                myskill_id = new String[js.length()];
                                cname = new String[js.length()];
                                sname = new String[js.length()];

                                val=new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    myskill_id[i] = u.getString("myskills_id");//dbcolumn name in double quotes
                                    cname[i] = u.getString("badgeorchive");//dbcolumn name in double quotes
                                    sname[i] = u.getString("skills");

                                    val[i] = "skills : "+sname[i]+"\n\nbadge or achive: "+cname[i] ;


                                }
                                ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, val);
                                ll.setAdapter(aa);

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
                //        params.put("fname", nams);//passing to python
                params.put("user_id", user_view_other_user.userid);
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
        ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mysk=myskill_id[i];
                final CharSequence[] items1 = {"Send Request"};

                AlertDialog.Builder builder = new AlertDialog.Builder(user_view_skill_cate.this);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items1[item].equals("Send Request")) {

                            String url = "http://" + sh.getString("ip", "") + "/api/viewskillandcate";
                            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                                    new Response.Listener<NetworkResponse>() {
                                        @Override
                                        public void onResponse(NetworkResponse response) {
                                            try {


                                                JSONObject obj = new JSONObject(new String(response.data));

                                                if (obj.getString("status").equals("ok")) {

                                                    Intent i = new Intent(getApplicationContext(), user_send_request.class);
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
//                                    params.put("pid", userid);//passing to python
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
                        } else if (items1[item].equals("update")){
//
//                            Intent i = new Intent(getApplicationContext(), user_add_skills.class);
//                            startActivity(i);



                        }
                    }
                });
                builder.show();
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


