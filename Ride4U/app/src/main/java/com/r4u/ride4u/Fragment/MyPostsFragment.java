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
    PostAdapter historyRideAdapter;
    PostAdapter activeRideAdapter;
    TextView listTitle;

    private boolean setup = false;

    public static ArrayList<Post> history = new ArrayList<>();
    public static ArrayList<Post> active = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myposts, container, false);

        initPostList(view);
        setupSwapButton(view);

        return view;
    }

    /**
     This function sets up a list view for displaying active and history rides.
     */
    private void setupListView() {
        historyRideAdapter = new PostAdapter(getActivity(), 0, history, Type.History);
        activeRideAdapter = new PostAdapter(getActivity(), 0, active, Type.Active);
        ride_history.setAdapter(historyRideAdapter);
        active_rides.setAdapter(activeRideAdapter);
    }

    /**
     This function sets up a toggle button for switching between displaying active rides and history rides.
     The active rides list view is initially set to be visible, while the history rides list view is set to be invisible.
     When the toggle button is clicked, the visibility of the list views is switched and the text of the list title is
     updated to reflect the currently displayed list.
     */
    private void setupSwapButton(View view) {
        listTitle = view.findViewById(R.id.listTitle);
        ToggleButton toggleButton = view.findViewById(R.id.toggle_active_history);
        toggleButton.setOnClickListener(v -> {
            if(toggleButton.isChecked()) {
                active_rides.setVisibility(View.GONE);
                ride_history.setVisibility(View.VISIBLE);
                listTitle.setText(requireContext().getString(R.string.historyRides));

            } else {
                active_rides.setVisibility(View.VISIBLE);
                ride_history.setVisibility(View.GONE);
                listTitle.setText(requireContext().getString(R.string.activeRides));
            }
        });

        active_rides.setVisibility(View.VISIBLE);
        ride_history.setVisibility(View.GONE);
        listTitle.setText(requireContext().getString(R.string.activeRides));
    }

    /**
     Iterate over firebase's posts, create each post and add it to an arraylist that belongs to him which is later used
     by the listview adapter to represent the posts onto the screen.
     */
    private void initPostList(View view) {
        active_rides = view.findViewById(R.id.history_);
        ride_history = view.findViewById(R.id.active_);
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {

                history = new ArrayList<>();
                active = new ArrayList<>();
                for(DataSnapshot toOrfrom : dataSnapShot.getChildren()) {
                    for(DataSnapshot cities : toOrfrom.getChildren()) {
                        for(DataSnapshot snapshot : cities.getChildren()) {
                            Post newPost = Post.createPost(snapshot);
                            classificationHistoryOrActive(newPost);
                        }
                    }
                }
                if (!setup) {
                    setupListView();
                    setup = true;
                }
                historyRideAdapter.notifyDataSetChanged();
                activeRideAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }
        });

    }

    /**
     This function classifies a given post as either an active ride or a history ride depending on the current date and time and the post's leaving date and time.
     @param newPost the post to classify as either active or history
     */
    private void classificationHistoryOrActive(Post newPost){
        if(Login.user.getId().equals(newPost.getPublisherID()) || newPost.getPassengerIDs().contains(Login.user.getId())) {
            String DateAndTime = DateAndTimeFormat.getDateAndTime(newPost.getLeavingDate() , newPost.getLeavingTime());
            if(DateAndTimeFormat.compareDateAndTime(DateAndTime, "GMT+1")) {
                history.add(newPost);
            }else {
                active.add(newPost);
            }
        }
    }

}