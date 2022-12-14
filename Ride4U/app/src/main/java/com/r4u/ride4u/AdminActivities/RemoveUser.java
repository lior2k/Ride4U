package com.r4u.ride4u.AdminActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.r4u.ride4u.Adapters.UsersAdapter;
import com.r4u.ride4u.R;
import com.r4u.ride4u.Objects.User;
import com.r4u.ride4u.UserActivities.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RemoveUser extends AppCompatActivity {
    final private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
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
        setContentView(R.layout.activity_remove_user);
        usersList = new ArrayList<>();
        initUsersList();
        setupBackButtonListener();
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
            User selectedUser = (User) parent.getAdapter().getItem(position);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("adminID", Login.user.getId());
                jsonObject.put("userID", selectedUser.getId());
                jsonObject.put("authID", selectedUser.getUid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServerFunctions removeUser = new ServerFunctions(jsonObject);
            removeUser.removeUserFromDB();
        });
    }

    private void initUsersList() {
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    usersList.add(new User(snapshot.child("firstname").getValue(String.class), snapshot.child("lastname").getValue(String.class), snapshot.child("email").getValue(String.class), snapshot.getKey(), false, snapshot.child("AuthUid").getValue(String.class), snapshot.child("deviceToken").getValue(String.class)));
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