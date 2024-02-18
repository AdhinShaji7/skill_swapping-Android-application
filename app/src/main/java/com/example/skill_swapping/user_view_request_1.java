package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

public class user_view_request_1 extends AppCompatActivity implements AdapterView.OnItemClickListener {
ListView ll;
String[] myskill_id,skill,username,amount,details,date,status,val;

public static String statuss,myskill_ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_request);
        ll=(ListView) findViewById(R.id.li);
        ll.setOnItemClickListener(this);

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url=sh.getString("url","")+"/user_view_request";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>(){
                    @Override
                    public void onResponse(NetworkResponse response) {


                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                myskill_id = new String[js.length()];
                                skill = new String[js.length()];
                                username = new String[js.length()];
                                amount = new String[js.length()];
                                details = new String[js.length()];
                                date = new String[js.length()];
                                status = new String[js.length()];
                                val=new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    myskill_id[i] = u.getString("myskills_id");//dbcolumn name in double quotes
                                    skill[i] = u.getString("skills");//dbcolumn name in double quotes
                                    username[i] = u.getString("ufname");
                                    amount[i] = u.getString("amount");
                                    details[i] = u.getString("details");
                                    date[i] = u.getString("date");
                                    status[i] = u.getString("status");
                                    val[i] = "skills : "+skill[i]+"\n\nusername    : "+username[i]+"\n\namount :"+amount[i]+"\n\ndetails :"+details[i]+"\n\ndate:"+date[i]+"\n\nstatus:"+status ;


                                }
//                                l1.setAdapter(new CustomCategory(getApplicationContext(), cid, cate));//custom_view_service.xml and li is the listview object
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
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //        params.put("fname", nams);//passing to python
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
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        statuss = status[i];
        myskill_ids = myskill_id[i];
        if (statuss.equals("pending")) {

            final CharSequence[] items = {"send amount to pay", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(user_view_request_1.this);
//        builder.setTitle("Take Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("send amount to pay")) {

                        startActivity(new Intent(getApplicationContext(), user_send_amts.class));

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
        else if (statuss.equals("paid")) {
            final CharSequence[] items = {"view payment","chat","call","video call","message","Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(user_view_request_1.this);
//        builder.setTitle("Take Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("send amount to pay")) {

                        startActivity(new Intent(getApplicationContext(), user_send_amts.class));

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        }

    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), userhome.class));
    }
}

