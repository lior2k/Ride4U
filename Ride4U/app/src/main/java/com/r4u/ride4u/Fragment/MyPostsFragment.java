package com.r4u.ride4u.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Adapters.RideAdapter;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class MyPostsFragment extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    ToggleButton toggleButton;
    ListView active_rides;
    ListView ride_history;
    RideAdapter HistoryRideAdapter;
    RideAdapter ActiveRideAdapter;


    public static ArrayList<Post> history = new ArrayList<>();
    public static ArrayList<Post> active = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myposts, container, false);

        setupListView(view);
        swapLists(toggleButton ,ride_history , active_rides);
        initPostList();

        return view;
    }
    private void setupListView(View view) {
        toggleButton = view.findViewById(R.id.toggle_active_history);
        active_rides = view.findViewById(R.id.history_);
        ride_history = view.findViewById(R.id.active_);
        HistoryRideAdapter = new RideAdapter(getContext(), 0, history);
        ActiveRideAdapter = new RideAdapter(getContext(), 0, active);
        ride_history.setAdapter(HistoryRideAdapter);
        active_rides.setAdapter(ActiveRideAdapter);

    }


    private void swapLists(ToggleButton toggleButton, ListView lv1, ListView lv2) {



        toggleButton.setOnClickListener(v -> {
            if(toggleButton.isChecked()) {
                lv1.setVisibility(View.GONE);
                lv2.setVisibility(View.VISIBLE);
            }
            else {
                lv1.setVisibility(View.VISIBLE);
                lv2.setVisibility(View.GONE);
            }
        });

    }

    private String getDateAndTime(String Date , String Time) {
        String [] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String [] Splited_String = Date.split("\\s+");
        int MonthNumber =  Arrays.asList(months).indexOf(Splited_String[1])+1;
        return  Splited_String[0]+"\\"+ MonthNumber +"\\"+Splited_String[2]+" "+Time;

    }

    private boolean CompareDateAndTime(String DateAndTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd\\MM\\yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("EET"));
        Date PostDate;
        try {
            PostDate = format.parse(DateAndTime);
            System.out.println(new Date());
            return (new Date().after(PostDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void initPostList() {
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                history = new ArrayList<>();
                active = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {

                    Post newPost = createPost(snapshot);
                    classificationHistoryOrActive(newPost);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }
        });

    }

    private Post createPost(DataSnapshot snapshot) {
        Post newPost = new Post(snapshot.getKey(), snapshot.child("publisherID").getValue(String.class), snapshot.child("publisherFirstName").getValue(String.class),
                snapshot.child("publisherLastName").getValue(String.class), snapshot.child("seats").getValue(String.class), snapshot.child("source").getValue(String.class),
                snapshot.child("destination").getValue(String.class), snapshot.child("leavingTime").getValue(String.class), snapshot.child("leavingDate").getValue(String.class),
                snapshot.child("cost").getValue(String.class), snapshot.child("description").getValue(String.class));
        for (DataSnapshot sp : snapshot.child("passengerIDs").getChildren()) {
            newPost.addPassenger(sp.getValue(String.class));
        }
        return newPost;
    }

    private void classificationHistoryOrActive(Post newPost){
        if(Login.user.getId().equals(newPost.getPublisherID()) || newPost.getPassengerIDs().contains(Login.user.getId())) {
            String DateAndTime = getDateAndTime(newPost.getLeavingDate() , newPost.getLeavingTime());
            if(!CompareDateAndTime(DateAndTime)) {
                history.add(newPost);
            }else {
                active.add(newPost);
            }

        }
    }





}
