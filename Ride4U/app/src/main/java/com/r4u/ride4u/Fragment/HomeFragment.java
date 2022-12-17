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
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;
import java.util.ArrayList;

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
        postAdapter = new PostAdapter(getContext(), 0, posts);
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

                    Post newPost = createPost(snapshot);

                    // if post is full don't show it (need to add time&date check)
                    if (!newPost.isFull())
                        posts.add(newPost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }

        });
    }

    // Create and return a new post built from a snapshot of the realtime firebase.
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
}