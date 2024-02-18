package com.example.skill_swapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.R.string;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.skill_swapping.user_chat_with_user;

public class user_view_request_chat extends Activity
{
    EditText ed1;
    ImageView b1;
    String chat;
    ListView l1;
    ImageView iv10;
    //    String method = "";
//    String namespace = "http://dbcon/";
//    String soapAction = "";
//    String url="";
    String[] aid,aname,r_id1,msg1;
    String[] msgid,s_id,r_id,message,date,re;

    String aid1,aname1,msg;
    SharedPreferences sh;
    //    String soapaction="";
    String contentcheck,chattedby;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_request_chat);
        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String url=sh.getString("url","")+"/user_chat_with_user";
        ed1=(EditText)findViewById(R.id.editText1);
        l1=(ListView)findViewById(R.id.listView1);
        b1=(ImageView)findViewById(R.id.imageView1);
        Toast.makeText(getApplicationContext(),"============="+sh.getString("lid", ""),Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"============="+sh.getString("receiver_id", ""),Toast.LENGTH_LONG).show();

//        Toast.makeText(getApplicationContext(),"============="+user_view_request.lg_idss,Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "hii1", Toast.LENGTH_SHORT).show();
//
//        startTimer();
//        getchats();

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                chat = ed1.getText().toString();
                if (chat.equalsIgnoreCase("")) {
                    ed1.setError("Empty Message ");
                    ed1.setFocusable(true);
                } else {

                    String url = sh.getString("url", "") + "/usersend_view_request_chat";
                    Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    try {
//                                    pd.dismiss();


                                        JSONObject obj = new JSONObject(new String(response.data));

                                        if (obj.getString("status").equals("ok")) {
                                            Toast.makeText(getApplicationContext(), " successssssssssssssssss", Toast.LENGTH_SHORT).show();
//
                                            startActivity(new Intent(getApplicationContext(), user_view_request_chat.class));
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
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            params.put("sender_id", sh.getString("lid", ""));//passing to python
                            params.put("receiver_id", sh.getString("receiver_id", ""));//passing to python
                            params.put("details", chat);//passing to python
                            return params;
                        }


                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            long imagename = System.currentTimeMillis();
                            //  params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);


                }
            }
        });


//
//                void startTimer ()
//            {
//                    timer = new Timer();
//                    initializeTimerTask();
//                    timer.schedule(timerTask, 0, 30000);
//                }
//
//                void initializeTimerTask () {
//                    timerTask = new TimerTask() {
//
//                        @Override
//                        public void run() {
//                            handler.post(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    // TODO Auto-generated method stub
//                                    getChats();
//                                }
//
//                                private void getChats() {
//                                }
//                            });
//                        }
//                    };
//                }

//                void getChats () {

        String url = sh.getString("url", "") + "/user_view_request_chat";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {


                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                s_id = new String[js.length()];
                                r_id = new String[js.length()];
                                message = new String[js.length()];

                                date = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    s_id[i] = u.getString("sender_id");//dbcolumn name in double quotes
                                    r_id[i] = u.getString("receiver_id");//dbcolumn name in double quotes

                                    message[i] = u.getString("message");
                                    date[i] = u.getString("date");
//                                    val[i] = "skills : "+skill[i]+"\n\nusername    : "+username[i]+"\n\namount :"+amount[i]+"\n\ndetails :"+details[i]+"\n\ndate:"+date[i]+"\n\nstatus:"+status[i] ;


                                }
                                l1.setAdapter(new Customchat(user_view_request_chat.this, message, s_id, date));//custom_view_service.xml and li is the listview object
//                                ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, val);
//                                ll.setAdapter(aa);

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

                params.put("sender_id", sh.getString("lid", ""));//passing to python
                params.put("receiver_id", sh.getString("receiver_id", ""));//passing to python

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


//
//            public void onBackPressed() {
//
////        super.onBackPressed();
//                if (sh.getString("usertype", "").equalsIgnoreCase("user")) {
//                    Intent b = new Intent(getApplicationContext(), userhome.class);
//                    startActivity(b);
//                }
////		else if(sh.getString("usertype","").equalsIgnoreCase("student")){
////			Intent b = new Intent(getApplicationContext(), Student_home.class);
////			startActivity(b);
////		}
//
//            }


//    private void getchats() {
//    }
////
//    private void startTimer() {
//    }
//    }

//
//    private void initializeTimerTask() {
//    }
    }
}
