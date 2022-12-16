package com.r4u.ride4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Adapters.PostAdapter;
import com.r4u.ride4u.Adapters.UsersAdapter;

import java.util.ArrayList;

public class RemoveUser extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

    ArrayList<User> usersList;
    UsersAdapter usersAdapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        searchView = findViewById(R.id.user_search_bar);

        initUsersList();
        setupListView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usersAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setupListView() {
        listView = findViewById(R.id.user_list_view);
        usersAdapter = new UsersAdapter(getApplicationContext(), 0, usersList);
        listView.setAdapter(usersAdapter);
    }

    private void initUsersList() {
        usersList = new ArrayList<>();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    usersList.add(new User(snapshot.child("firstname").getValue(String.class), snapshot.child("lastname").getValue(String.class),
                            snapshot.child("email").getValue(String.class), snapshot.getKey(), false));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}