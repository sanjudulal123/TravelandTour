package com.death.tnt.map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.death.tnt.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class InListView extends AppCompatActivity {
    Button search;
    ListView inlistview;
    //step 1: definng a RequestQueue
    RequestQueue requestqueue;
    ArrayList<InListModule> listdata = new ArrayList<>();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inlistview);
        search = (Button)findViewById(R.id.search);
        inlistview = (ListView)findViewById(R.id.inlistView);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        final Double lat1 = bundle.getDouble("lat1");
        final Double lon1 = bundle.getDouble("lon1");
        //step 2: define a URL source
        final String url = bundle.getString("iurl");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //step 3 : Initializing requestqueue
        requestqueue = Volley.newRequestQueue(this);
        //step 4: defining a stringrequest
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("IURL",url);
                    JSONObject first = new JSONObject(response);
                    String status = first.getString("status");
                    if (status.equals("OVER_QUERY_LIMIT")) {
                        pDialog.dismiss();
                        Toast.makeText(InListView.this, "Query Limit has been reached", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("ZERO_RESULTS")) {
                        pDialog.dismiss();
                        Toast.makeText(InListView.this, "No results found for your search", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("INVALID_REQUEST")) {
                        pDialog.dismiss();
                        Toast.makeText(InListView.this, "invalid request", Toast.LENGTH_SHORT).show();
                    } else if(status.equals("OK")){
                        pDialog.dismiss();
                    JSONArray resultsarray = first.getJSONArray("results");
                    for (int i = 0; i < resultsarray.length(); i++) {
                        JSONObject second = resultsarray.getJSONObject(i);
                        String icon_url = second.getString("icon");
                        String name = second.getString("name");
                        float rating = second.getInt("rating");
                        String str_location = second.getString("vicinity");
                        JSONObject opening_hours = second.getJSONObject("opening_hours");
                        boolean open_now = opening_hours.getBoolean("open_now");
                        JSONObject geometry = second.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double lat2 = location.getDouble("lat");
                        double lon2 = location.getDouble("lng");
                        InListModule inListModule = new InListModule();
                        inListModule.setIcon_url(icon_url);
                        inListModule.setName(name);
                        inListModule.setRating(rating);
                        inListModule.setLocation(str_location);
                        inListModule.setOpen_now(open_now);
                        inListModule.setLat1(lat1);
                        inListModule.setLng1(lon1);
                        inListModule.setLat2(lat2);
                        inListModule.setLng2(lon2);
                        listdata.add(inListModule);
                    }
                    pDialog.dismiss();
                    inlistview.setAdapter(new InListAdapter(InListView.this,R.layout.in_list_adapter,listdata));
                } else {
                        pDialog.dismiss();
                        Toast.makeText(InListView.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    pDialog.dismiss();
                    Toast.makeText(InListView.this, "ERROR! = "+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(InListView.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });
        requestqueue.add(stringRequest);
    }
}
