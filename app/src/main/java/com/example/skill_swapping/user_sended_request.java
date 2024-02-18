package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.telephony.SmsManager;
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

//import io.agora.rtc.RtcEngine;


public class user_sended_request extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView ll;
    String[] log_id,req_id,myskill_id,skill,user_id,amount,details,date,status,val,user_name,phones;
    SharedPreferences sh;
    public static String statuss,myskill_ids,req_ids,amts,lg_ids,phsss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sended_request);
        ll=(ListView) findViewById(R.id.li_send_req);
        ll.setOnItemClickListener(this);

         sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url=sh.getString("url","")+"/user_view_sended_request";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>(){
                    @Override
                    public void onResponse(NetworkResponse response) {


                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                req_id = new String[js.length()];
                                myskill_id = new String[js.length()];
                                skill = new String[js.length()];
                                user_id = new String[js.length()];
                                user_name = new String[js.length()];
                                log_id = new String[js.length()];
                                amount = new String[js.length()];
                                details = new String[js.length()];
                                date = new String[js.length()];
                                status = new String[js.length()];
                                val=new String[js.length()];
                                phones=new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    req_id[i] = u.getString("request_id");//dbcolumn name in double quotes
                                    myskill_id[i] = u.getString("myskills_id");//dbcolumn name in double quotes
                                    skill[i] = u.getString("skills");//dbcolumn name in double quotes
                                    user_id[i] = u.getString("user_id");
                                    log_id[i] = u.getString("login_id");
                                    amount[i] = u.getString("amount");
                                    details[i] = u.getString("details");
                                    date[i] = u.getString("date");
                                    status[i] = u.getString("status");
                                    phones[i] = u.getString("phone");
                                    user_name[i] = u.getString("ufname")+ " "+u.getString("ulname");

                                    val[i] = "skills : "+skill[i]+"\n\nuser:"+user_name[i]+"\n\namount :"+amount[i]+"\n\ndetails :"+details[i]+"\n\ndate:"+date[i]+"\n\nstatus:"+status[i] ;


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
                params.put("login_id",sh.getString("lid",""));
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
        req_ids=req_id[i];
        amts=amount[i];
        lg_ids=log_id[i];
        phsss=phones[i];
//        String lids=log_id[i];
        SharedPreferences.Editor ed = sh.edit();
        ed.putString("receiver_id", lg_ids);
        ed.commit();
        Toast.makeText(getApplicationContext(),"============="+lg_ids,Toast.LENGTH_LONG).show();

        if (statuss.equals("amount_sended")) {

            final CharSequence[] items = {"make payment", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(user_sended_request.this);
//        builder.setTitle("Take Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("make payment")) {

                        startActivity(new Intent(getApplicationContext(), user_make_payment.class));

                    }


                    else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
        else if (statuss.equals("paid")) {
            final CharSequence[] items = {"chat","call","video call","message","Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(user_sended_request.this);
//        builder.setTitle("Take Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("chat")) {

                        startActivity(new Intent(getApplicationContext(), user_chat_with_user.class));
                    }
                    else if (items[item].equals("call")) {
                            Toast.makeText(getApplicationContext(), "Not found"+phsss, Toast.LENGTH_LONG).show();

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+phsss));//change the number
                            startActivity(callIntent);



                    }
                    else if (items[item].equals("message")) {
                        Toast.makeText(getApplicationContext(), "Not found"+phsss, Toast.LENGTH_LONG).show();
//                     PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
                        startActivity(new Intent(getApplicationContext(), user_sended_msg.class));



                    }


                    else if (items[item].equals("video call")) {
                        Toast.makeText(getApplicationContext(), "Not found"+phsss, Toast.LENGTH_LONG).show();

                        Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phsss));
                        callIntent.putExtra("videocall", true);
                        startActivity(callIntent);
                    }



                    else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        }

    }
}

