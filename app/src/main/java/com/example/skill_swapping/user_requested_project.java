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

public class user_requested_project extends AppCompatActivity {
ListView l1;
SharedPreferences sh;
    String[] pro_name,date,value,status,uid,pid,req,price;
    public  static String user_id,preq_id,amount,statusss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_requested_project);
        l1=findViewById(R.id.listview);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ip","");
        String url=sh.getString("url","")+"/View_Requested_Projects";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {

                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                pro_name = new String[js.length()];
                                pid = new String[js.length()];
                                uid=new String[js.length()];
                                req=new String[js.length()];
                                date = new String[js.length()];
                                status = new String[js.length()];
                                price = new String[js.length()];
                                value = new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    pid[i] = u.getString("project_id");
                                    price[i] = u.getString("amount");
                                    pro_name[i] = u.getString("projects");
                                    uid[i] = u.getString("user_id");
                                    req[i] = u.getString("prequest_id");
                                    user_id=uid[i];
                                    date[i] = u.getString("date");//dbcolumn name in double quotes
                                    status[i] = u.getString("stat");

                                    value[i] = "\nproject name : " + pro_name[i] + "\ndate : " + date[i]+ "\nstatus : " + status[i];
                                }

                                l1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
//                                l2.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
//                                ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, value);
//                                l2.setAdapter(aa);
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
                params.put("req_id", user_requested_project.preq_id);//passing to python
                params.put("user_id", user_requested_project.user_id);//passing to python


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
                amount=price[i];
                preq_id=req[i];
                statusss=status[i];

                if(statusss.equalsIgnoreCase("pending"))
                {

                    final CharSequence[] items1 = {"Add Issues","Cancel"};
                }
                else {
                    final CharSequence[] items1 = {"Add Issues","Make Payment","Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(user_requested_project.this);
                    builder.setItems(items1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items1[item].equals("Add Issues")) {

                                Intent i = new Intent(getApplicationContext(), user_add_issue.class);
                                startActivity(i);


                            } else if (items1[item].equals("Cancel")) {
                                dialog.dismiss();
                            } else if (items1[item].equals("Make Payment")) {
//
                                Intent i = new Intent(getApplicationContext(), user_make_payment.class);
                                startActivity(i);


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