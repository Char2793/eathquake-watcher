package com.example.charu.earthquakewatcher.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.charu.earthquakewatcher.Activities.Model.Constants;
import com.example.charu.earthquakewatcher.Activities.Model.Earthquake;
import com.example.charu.earthquakewatcher.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity {


    private ArrayList<String> arrayList;
    private ListView quakes_list;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;

    private List<Earthquake> earthquakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quakes_list);

        earthquakeList=new ArrayList<>();
        quakes_list=(ListView)findViewById(R.id.quakes_list);

        queue= Volley.newRequestQueue(this);

        arrayList=new ArrayList<>();


        getAllQuakes(Constants.URL);

    }

    void getAllQuakes(String url)
    {


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Earthquake earthquake=new Earthquake();


                try {
                    JSONArray array = response.getJSONArray("features");
                        for (int i = 0; i < Constants.LIMIT; i++) {
        JSONObject properties = array.getJSONObject(i).getJSONObject("properties");
        // Log.d("Properties",properties.getString("place"));

        JSONObject geometry = array.getJSONObject(i).getJSONObject("geometry");

        JSONArray coordinates = geometry.getJSONArray("coordinates");

        double longit = coordinates.getDouble(0);
        double lat = coordinates.getDouble(1);
        //Log.d("Quake",lat + " " + longit);

        earthquake.setPlace(properties.getString("place"));
        earthquake.setType(properties.getString("type"));
        earthquake.setTime(properties.getLong("time"));
        earthquake.setLat(lat);
        earthquake.setLongit(longit);
//        earthquake.setMagnitude(properties.getDouble("mag"));
//        earthquake.setDetailLink(properties.getString("detail"));

                            arrayList.add(earthquake.getPlace());


    }

    arrayAdapter=new ArrayAdapter(QuakesListActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList);
                        quakes_list.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();

}catch (JSONException e){
e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

    }
}
