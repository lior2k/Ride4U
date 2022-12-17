package com.r4u.ride4u.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.r4u.ride4u.R;

import java.util.List;

public class RideFragment extends Fragment {

    ToggleButton toggleButton;
    ListView active_rides;
    ListView ride_history;

//    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data1);
//    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data2);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ride, container, false);
        toggleButton = (ToggleButton) view.findViewById(R.id.toggle_active_history);
        active_rides = (ListView) view.findViewById(R.id.active_);
        ride_history = (ListView) view.findViewById(R.id.history_);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapLists(v, toggleButton, active_rides, ride_history);
            }
        });
        return view;
    }


    private void swapLists(View view, ToggleButton toggleButton, ListView lv1, ListView lv2) {
        if(toggleButton.isChecked()) {
            lv1.setVisibility(View.GONE);
            lv2.setVisibility(View.VISIBLE);
        }
        else {
            lv1.setVisibility(View.VISIBLE);
            lv2.setVisibility(View.GONE);
        }
    }

}
