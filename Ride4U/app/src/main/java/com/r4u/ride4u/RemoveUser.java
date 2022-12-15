package com.r4u.ride4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemoveUser extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

    ArrayList<User> arrayList;
    ArrayAdapter adapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        searchView = findViewById(R.id.user_search_bar);
        listView = findViewById(R.id.user_list_view);
        arrayList = new ArrayList<>();
        initArrayList();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.users_list, arrayList);

        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void initArrayList() {
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    arrayList.add(new User(snapshot.child("firstname").getValue(String.class), snapshot.child("lastname").getValue(String.class),
                            snapshot.child("email").getValue(String.class), snapshot.getKey(), false));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}