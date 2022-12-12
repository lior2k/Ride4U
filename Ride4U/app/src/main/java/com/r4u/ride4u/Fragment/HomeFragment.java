package com.r4u.ride4u.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Post;
import com.r4u.ride4u.PostActivity;
import com.r4u.ride4u.R;
import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    public static ArrayList<Post> posts;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initPostList();
        setupListView(view);
        setupListListener();

        searchView = view.findViewById(R.id.search_bar);

        return view;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//
//    }

    private void setupListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent postActivity = new Intent(getContext(), PostActivity.class);
                postActivity.putExtra("index", String.valueOf(position));
                startActivity(postActivity);
            }
        });
    }

    private void setupListView(View view) {
        listView = view.findViewById(R.id.list_view);
        PostAdapter postAdapter = new PostAdapter(getContext(), 0, posts);
        listView.setAdapter(postAdapter);
    }

    private void initPostList() {
        posts = new ArrayList<>();
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {

                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    posts.add(new Post(snapshot.getKey(), snapshot.child("firstname").getValue(String.class),
                            snapshot.child("lastname").getValue(String.class), "desc", snapshot.child("src").getValue(String.class),
                            snapshot.child("dest").getValue(String.class), Integer.parseInt(Objects.requireNonNull(snapshot.child("seats").getValue(String.class))),
                            snapshot.child("time").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }
        });
    }
}