package com.r4u.ride4u.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Adapters.UsersAdapter;
import com.r4u.ride4u.Objects.User;
import com.r4u.ride4u.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    ArrayList<User> usersList;
    UsersAdapter usersAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();

    // on first launch setup listview and searchview
    public boolean setup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setupBackButtonListener();
        initUsersList();
    }

    private void setupSearchView() {
        searchView = findViewById(R.id.user_search_bar);
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
        setupItemFromListView();
    }

    private void setupItemFromListView() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent chatRoom = new Intent(Chat.this, ChatRoom.class);
            User selectedUser = (User) parent.getAdapter().getItem(position);
            chatRoom.putExtra("userName", selectedUser.getFullName());
            chatRoom.putExtra("userId", selectedUser.getId());
            startActivity(chatRoom);
        });
    }

    private void initUsersList() {

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = new User(snapshot.child("firstname").getValue(String.class), snapshot.child("lastname").getValue(String.class),
                            snapshot.child("email").getValue(String.class), snapshot.getKey(), false, snapshot.child("AuthUid").getValue(String.class), snapshot.child("deviceToken").getValue(String.class));
                    if (!user.getId().equals(Login.user.getId())) {
                        usersList.add(user);
                    }
                }

                if (!setup) {
                    setupListView();
                    setupSearchView();
                    setup = true;
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupBackButtonListener() {
        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

}