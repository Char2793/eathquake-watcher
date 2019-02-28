package com.example.charu.earthquakewatcher.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.charu.earthquakewatcher.Activities.Model.Constants;
import com.example.charu.earthquakewatcher.Activities.Model.CustomInfoWindow;
import com.example.charu.earthquakewatcher.Activities.Model.Earthquake;
import com.example.charu.earthquakewatcher.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private RequestQueue queue;
    private AlertDialog dialog;
    private BitmapDescriptor[] bitmapDescriptors;
    private Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        show=(Button)findViewById(R.id.btn_show);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapsActivity.this, QuakesListActivity.class);
                startActivity(intent);
            }
        });


        bitmapDescriptors=new BitmapDescriptor[]{
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
        };


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        queue= Volley.newRequestQueue(this);

        getEarthquakes();
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);

        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT<23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }

        // Add a marker in Sydney and move the camera

    }

    private void getEarthquakes() {


        final Earthquake earthquake=new Earthquake();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Constants.URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array=response.getJSONArray("features");
                    for (int i=0;i<Constants.LIMIT;i++)
                    {
                        JSONObject properties=array.getJSONObject(i).getJSONObject("properties");
                       // Log.d("Properties",properties.getString("place"));

                        JSONObject geometry=array.getJSONObject(i).getJSONObject("geometry");

                        JSONArray coordinates=geometry.getJSONArray("coordinates");

                        double longit=coordinates.getDouble(0);
                        double lat=coordinates.getDouble(1);
                        //Log.d("Quake",lat + " " + longit);

                        earthquake.setPlace(properties.getString("place"));
                        earthquake.setType(properties.getString("type"));
                        earthquake.setTime(properties.getLong("time"));
                        earthquake.setLat(lat);
                        earthquake.setLongit(longit);
                        earthquake.setMagnitude(properties.getDouble("mag"));
                        earthquake.setDetailLink(properties.getString("detail"));

                        java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
                        String newDate=dateFormat.format(new Date(Long.valueOf(properties.getLong("time"))).getTime());




                        MarkerOptions markerOptions=new MarkerOptions();
                        markerOptions.icon(bitmapDescriptors[Constants.RandomInt(bitmapDescriptors.length,0)]);
                        markerOptions.title(earthquake.getPlace());
                        markerOptions.position(new LatLng(lat,longit));
                        markerOptions.snippet("Magnitude: " + earthquake.getMagnitude() + "\n" + "Date: " + newDate);



                        if(earthquake.getMagnitude()>=4.0)
                        {
                            CircleOptions circleOptions=new CircleOptions();
                            circleOptions.center(new LatLng(earthquake.getLat(),earthquake.getLongit()));
                            circleOptions.radius(30000);
                            circleOptions.fillColor(Color.RED);
                            circleOptions.strokeWidth(3.6f);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMap.addCircle(circleOptions);
                        }


                        Marker marker=mMap.addMarker(markerOptions);
                        marker.setTag(earthquake.getDetailLink());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,longit),1));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);




    }

    @Override
    public void onInfoWindowClick(Marker marker) {

       // Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_LONG).show();
        getQuakeDetails(marker.getTag().toString());


    }

    public void getQuakeDetails(String url) {


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String detailURl="";

                        try {
                            JSONObject properties=response.getJSONObject("properties");
                            JSONObject products=properties.getJSONObject("products");
                            JSONArray geoserve=products.getJSONArray("geoserve");

                            for(int i=0;i<geoserve.length();i++)
                            {
                                JSONObject geoserveObj=geoserve.getJSONObject(i).getJSONObject("contents");

                               // JSONObject contents=geoserveObj.getJSONObject("contents");
                                JSONObject geoservejson=geoserveObj.getJSONObject("geoserve.json");
                                detailURl=geoservejson.getString("url");
                                //Log.i("URl",detailURl);

                            }

                            getMoreDetails(detailURl);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);



    }

    public void getMoreDetails(String url)
    {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AlertDialog.Builder dialogbuilder=new AlertDialog.Builder(MapsActivity.this);
                View view=getLayoutInflater().inflate(R.layout.popup,null);

                Button dismiss=(Button)view.findViewById(R.id.popup_dismiss);
                Button cancel=(Button)view.findViewById(R.id.pop_cancel);
                TextView city_list=(TextView)view.findViewById(R.id.pop_list);
                WebView webView=(WebView)view.findViewById(R.id.web_view);

                StringBuilder stringBuilder=new StringBuilder();

                try {

                    if(response.has("tectonicSummary") && response.getString("tectonicSummary")!=null)
                    {
                        JSONObject tectonics=response.getJSONObject("tectonicSummary");
                        if(tectonics.has("text") && tectonics.getString("text")!=null)
                        {

                            String text=tectonics.getString("text");
                            webView.loadDataWithBaseURL(null,text,"text/html","UTF-8",null);

                        }


                    }



                    JSONArray cities=response.getJSONArray("cities");
                    for(int i=0;i<cities.length();i++)
                    {
                        JSONObject jsonObject=cities.getJSONObject(i);

                        stringBuilder.append("Name of the city: " + jsonObject.getString("name")+ "Distance: " + jsonObject.getString("distance") +
                        "Population: " + jsonObject.getString("population"));

                        stringBuilder.append("\n\n");


                        }
                        city_list.setText(stringBuilder);

                    dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });



                    dialogbuilder.setView(view);

                     dialog=dialogbuilder.create();

                    dialog.show();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

}
