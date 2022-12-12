package com.r4u.ride4u.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.r4u.ride4u.R;
// https://www.youtube.com/watch?v=O17fvn7kztg&ab_channel=UiLoverAndroid - profile tutorial
public class ProfileFragment extends Fragment {

    private String email;
    private String name;
    private String last_name;
    private int id;
    DatabaseReference databaseReference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

}