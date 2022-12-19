package com.r4u.ride4u.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
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
import com.r4u.ride4u.UserActivities.MainActivity;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    PostAdapter postAdapter;
    public static ArrayList<Post> posts = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initPostList();
        setupListView(view);
        setupSearchView(view);

        return view;
    }

    private void setupSearchView(View view) {
        searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                postAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    // Create a new PostAdapter and set it to be the listview adapter.
    private void setupListView(View view) {
        listView = view.findViewById(R.id.list_view);
        postAdapter = new PostAdapter(getContext(), 0, posts, true);
        listView.setAdapter(postAdapter);
    }


    // Iterate over firebase's posts, create each post and add it to an arraylist which is later used
    // by the listview adapter to represent the posts onto the screen.
    private void initPostList() {
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                posts = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {

                    Post newPost = Post.createPost(snapshot);

                    // if post is full or user already joined or time has passed - don't show it
                    String date_time = DateAndTimeFormat.getDateAndTime(newPost.getLeavingDate(), newPost.getLeavingTime());
                    if (!newPost.isFull() && !DateAndTimeFormat.compareDateAndTime(date_time, "EET"))
                        posts.add(newPost);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }

        });
    }

}