package com.example.charu.earthquakewatcher.Activities.Model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.charu.earthquakewatcher.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import static com.example.charu.earthquakewatcher.R.layout.custom_info_window;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private View view;

    public CustomInfoWindow(Context context)
    {
        this.context=context;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(R.layout.custom_info_window,null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView title=(TextView)view.findViewById(R.id.title_info);
        title.setText(marker.getTitle());
        TextView mag=(TextView)view.findViewById(R.id.magnitude);
        mag.setText(marker.getSnippet());
        Button more_info=(Button)view.findViewById(R.id.btn_more_info);

        return view;
    }
}
