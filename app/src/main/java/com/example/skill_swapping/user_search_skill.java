package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class user_search_skill extends AppCompatActivity {
EditText e1;
ListView l1;
SharedPreferences sh;
String[]  name,skill,badgeorachive,uid,value,skill_id;
public static String sk_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_skill);
        e1=findViewById(R.id.editTextTextPersonName4);
        l1=findViewById(R.id.listview);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



        sh.getString("ip","");

        String url=sh.getString("url","")+"/user_search_user";

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
                                name = new String[js.length()];
                                skill = new String[js.length()];
                                badgeorachive = new String[js.length()];
                                skill_id = new String[js.length()];


                                value = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    skill_id[i] = u.getString("myskills_id");
                                    uid[i] = u.getString("user_id");
                                    name[i] = u.getString("ufname");
                                    skill[i] = u.getString("skills");//dbcolumn name in double quotes
                                    badgeorachive[i] = u.getString("badgeorchive");



                                    value[i] = "\nName : " + name[i] + "\nskill : " + skill[i]+ "\nbadge or achive : " + badgeorachive[i];
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
                params.put("login_id", sh.getString("lid",""));//passing to python
                params.put("user_id", sh.getString("uid",""));//passing to python
//                params.put("proid",oth_pid);//passing to python


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
//                oth_pid=proid[i];
                sk_id=skill_id[i];
                final CharSequence[] items1 = {"Send Request","Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(user_search_skill.this);
                builder.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items1[item].equals("Send Request")) {
                            Intent i = new Intent(getApplicationContext(), user_search_sendrequest.class);
                            startActivity(i);


//                                if (items1[item].equals("Delete"))


                        } else if (items1[item].equals("Cancel")) {
                            dialog.dismiss();
                        } else if (items1[item].equals("")){
//
//                            Intent i = new Intent(getApplicationContext(), user_add_skills.class);
//                            startActivity(i);



                        }
                    }
                });
                builder.show();
            }


        });
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                try {
                String out = e1.getText().toString();
                sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sh.getString("ip","");
                String url = "http://" + sh.getString("ip", "") + "/api/search_skill";
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
                                        name = new String[js.length()];
                                        skill = new String[js.length()];
                                        badgeorachive = new String[js.length()];
                                        skill_id = new String[js.length()];


                                        value = new String[js.length()];


                                        for (int i = 0; i < js.length(); i++) {
                                            JSONObject u = js.getJSONObject(i);
                                            skill_id[i] = u.getString("myskills_id");
                                            uid[i] = u.getString("user_id");
                                            name[i] = u.getString("ufname");
                                            skill[i] = u.getString("skills");//dbcolumn name in double quotes
                                            badgeorachive[i] = u.getString("badgeorchive");



                                            value[i] = "\nName : " + name[i] + "\nskill : " + skill[i]+ "\nbadge or achive : " + badgeorachive[i];

//                                    Toast.makeText(getApplicationContext(), "evida ethi epo", Toast.LENGTH_LONG).show();
                                        }

//                                Toast.makeText(getApplicationContext(), "custom image ina kanduuu", Toast.LENGTH_LONG).show();
                                        l1.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
//                                l2.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, value));
//                                ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, value);
//                                l2.setAdapter(aa);
                                    } else {
//                                            Toast.makeText(getApplicationContext(), "Not found"+s_palce, Toast.LENGTH_LONG).show();
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
                        params.put("login_id", sh.getString("lid",""));
                        params.put("skillorbadge", out);

                        //passing to python
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