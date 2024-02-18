package com.example.skill_swapping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class user_add_skills extends AppCompatActivity {
    Button b1;
    FloatingActionButton f1;
    Spinner s1, s2;
    String[] cid, cname, value, sid, sname, value2;
    SharedPreferences sh;
    int selectedPositionSpinner1 = 0;
    int selectedPositionSpinner2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_skills);
        b1 = findViewById(R.id.button4);
        s1 = findViewById(R.id.spinner);
        s2 = findViewById(R.id.spinner2);
        f1=findViewById(R.id.floatingActionButton);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), user_view_skill.class));

            }
        });

        // Move the spinner population logic to a separate method
        populateSpinner(s1);
        populateSpinner2(s2);

        // Set default selections for the spinners to "Select"
        s1.setSelection(0);
        s2.setSelection(0);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sh.getString("ip", "");
                String url = sh.getString("url", "") + "/add_skill";

                // Check if the user selected the "Select" option for both spinners
                if (selectedPositionSpinner1 == 0 || selectedPositionSpinner2 == 0) {
                    // Show a validation message
                    Toast.makeText(getApplicationContext(), "Please select valid options", Toast.LENGTH_SHORT).show();
                } else {
                    // Use the selectedPosition variable in your request parameters
                    sendSelectedIdsToPython(url, sh.getString("lid", ""), sh.getString("user_id", ""), cid[selectedPositionSpinner1], sid[selectedPositionSpinner2]);
                }
            }
        });

    }

    private void populateSpinner2(Spinner spinner) {
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ip", "");
        String url = sh.getString("url", "") + "/viewskill";

        // Make a network request to fetch data for the spinner
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                JSONArray js = jsonObj.getJSONArray("data");
                                sid = new String[js.length() + 1];  // Increase the size of the array
                                sname = new String[js.length() + 1];
                                value2 = new String[js.length() + 1];

                                // Adding "Select" as the first item
                                sid[0] = "0";
                                sname[0] = "Select skill";
                                value2[0] = sname[0];

                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    sid[i + 1] = u.getString("skills_id");
                                    sname[i + 1] = u.getString("skills");
                                    value2[i + 1] = sname[i + 1]; // Use category names in the spinner
                                }

                                // Use an ArrayAdapter to set data to the spinner
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, value2);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);

                                // Set a listener to capture the selected item position
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        // Update the selectedPosition variable
                                        selectedPositionSpinner2 = position;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // Do nothing here
                                    }
                                });
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
                        // Handle error
                        Toast.makeText(getApplicationContext(), "Error in network request", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login_id", sh.getString("lid", ""));
                params.put("user_id", sh.getString("user_id", ""));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
    }

    private void populateSpinner(Spinner spinner) {
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ip", "");
        String url = sh.getString("url", "") + "/viewcate";

        // Make a network request to fetch data for the spinner
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObj = new JSONObject(new String(response.data));
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                JSONArray js = jsonObj.getJSONArray("data");
                                cid = new String[js.length() + 1];  // Increase the size of the array
                                cname = new String[js.length() + 1];
                                value = new String[js.length() + 1];

                                // Adding "Select" as the first item
                                cid[0] = "0";
                                cname[0] = "Select Badge or achive";
                                value[0] = cname[0];

                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    cid[i + 1] = u.getString("badgeorchive_id");
                                    cname[i + 1] = u.getString("badgeorchive");
                                    value[i + 1] = cname[i + 1]; // Use category names in the spinner
                                }

                                // Use an ArrayAdapter to set data to the spinner
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, value);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);

                                // Set a listener to capture the selected item position
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                        // Update the selectedPosition variable
                                        selectedPositionSpinner1 = position;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parentView) {
                                        // Do nothing here
                                    }
                                });
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
                        // Handle error
                        Toast.makeText(getApplicationContext(), "Error in network request", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =  new HashMap<>();
                params.put("login_id", sh.getString("lid", ""));
                params.put("user_id", sh.getString("user_id", ""));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
    }

    private void sendSelectedIdsToPython(String url, String loginId, String user_Id, String selectedCid, String selectedSid) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            if (obj.getString("status").equals("ok")) {
                                Toast.makeText(getApplicationContext(), " success", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), userhome.class);
                                startActivity(i);
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
                        // Handle error
                        Toast.makeText(getApplicationContext(), "Error in network request", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login_id", loginId);
                params.put("user_id", user_Id);
                params.put("cid", selectedCid);
                params.put("sid", selectedSid);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), userhome.class));
    }
}
