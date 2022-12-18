package com.r4u.ride4u.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Adapters.PostAdapter;
import com.r4u.ride4u.Adapters.DateAndTimeFormat;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;

import java.util.ArrayList;

public class MyPostsFragment extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    ListView active_rides;
    ListView ride_history;
    PostAdapter HistoryRideAdapter;
    PostAdapter ActiveRideAdapter;
    TextView listTitle;

    public static ArrayList<Post> history = new ArrayList<>();
    public static ArrayList<Post> active = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myposts, container, false);

        setupListView(view);
        setupSwapButton(view);

        active_rides.setVisibility(View.VISIBLE);
        ride_history.setVisibility(View.GONE);
        listTitle = view.findViewById(R.id.listTitle);
        listTitle.setText("Active Rides");

        initPostList();

        return view;
    }

    private void setupListView(View view) {
        active_rides = view.findViewById(R.id.history_);
        ride_history = view.findViewById(R.id.active_);
        HistoryRideAdapter = new PostAdapter(getContext(), 0, history, false);
        ActiveRideAdapter = new PostAdapter(getContext(), 0, active, false);
        ride_history.setAdapter(HistoryRideAdapter);
        active_rides.setAdapter(ActiveRideAdapter);
    }


    private void setupSwapButton(View view) {
        ToggleButton toggleButton = view.findViewById(R.id.toggle_active_history);
        toggleButton.setOnClickListener(v -> {
            if(toggleButton.isChecked()) {
                active_rides.setVisibility(View.GONE);
                ride_history.setVisibility(View.VISIBLE);
                listTitle.setText("History Rides");
            } else {
                active_rides.setVisibility(View.VISIBLE);
                ride_history.setVisibility(View.GONE);
                listTitle.setText("Active Rides");
            }
        });

    }

    private void initPostList() {
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                history = new ArrayList<>();
                active = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {

                    Post newPost = Post.createPost(snapshot);
                    classificationHistoryOrActive(newPost);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }
        });

    }

    private void classificationHistoryOrActive(Post newPost){
        if(Login.user.getId().equals(newPost.getPublisherID()) || newPost.getPassengerIDs().contains(Login.user.getId())) {
            String DateAndTime = DateAndTimeFormat.getDateAndTime(newPost.getLeavingDate() , newPost.getLeavingTime());
            if(DateAndTimeFormat.CompareDateAndTime(DateAndTime, "GMT+1")) {
                history.add(newPost);
            }else {
                active.add(newPost);
            }
        }
    }

}