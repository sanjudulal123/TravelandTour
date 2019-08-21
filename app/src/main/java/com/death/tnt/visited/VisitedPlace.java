package com.death.tnt.visited;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class VisitedPlace extends Fragment {
    ListView listView;
    ProgressDialog pDialog;
    ArrayList<RedditModule> ardata = new ArrayList<>();
    //step 1: definng a RequestQueue
    RequestQueue requestqueue;
    //step 2: define a URL source
    String url = "https://www.reddit.com/r/NepalPics.json";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reddit_list_view,null);
        listView = (ListView)view.findViewById(R.id.reddit_list_view);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        //step 3 : Initializing requestqueue
        requestqueue = Volley.newRequestQueue(getActivity());
        //step 4: defining a stringrequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject first = new JSONObject(response);
                    JSONObject dataobject = first.getJSONObject("data");//"data":{
                    JSONArray childrenarray = dataobject.getJSONArray("children");//"children":[
                    for (int i = 0; i < childrenarray.length(); i++) {
                        JSONObject second = childrenarray.getJSONObject(i);
                        JSONObject data = second.getJSONObject("data");
                        RedditModule redditModule = new RedditModule();
                        redditModule.setCaption(data.getString("title"));
                        redditModule.setImage_url(data.getString("url"));
                        redditModule.setScore(data.getInt("score"));
                        String comment_link ="https://www.reddit.com"+data.getString("permalink")+".json";
                        redditModule.setComment_link(comment_link);
                        redditModule.setNo_of_comments(data.getInt("num_comments"));
                        ardata.add(redditModule);
                    }
                    pDialog.dismiss();
                    listView.setAdapter(new MyRedditAdapter(getActivity(),R.layout.reddit_list_view_item,ardata));


                } catch (Exception e) {
                    pDialog.dismiss();
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });
        //step 5: add stringRequest to RequestQueue
        requestqueue.add(stringRequest);
        return view;
    }
}
