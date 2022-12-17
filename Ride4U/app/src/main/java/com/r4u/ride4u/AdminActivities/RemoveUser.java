package com.r4u.ride4u.AdminActivities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Adapters.UsersAdapter;
import com.r4u.ride4u.R;
import com.r4u.ride4u.Objects.User;
import com.r4u.ride4u.UserActivities.Login;
import java.util.ArrayList;

public class RemoveUser extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

    ArrayList<User> usersList;
    UsersAdapter usersAdapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    FirebaseAuth autoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        initUsersList();
        setupListView();
        setupSearchView();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = (User) parent.getAdapter().getItem(position);
                removeUserFromRealTimeDB(selectedUser);
                removeUserFromAuthDB(selectedUser.getUid());

            }
        });
    }

    private void removeUserFromAuthDB(String Uid) {
        autoProfile = FirebaseAuth.getInstance();
    }


    private void removeUserFromRealTimeDB(User user) {

    }

    private void initUsersList() {
        usersList = new ArrayList<>();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    usersList.add(new User(snapshot.child("firstname").getValue(String.class), snapshot.child("lastname").getValue(String.class),
                            snapshot.child("email").getValue(String.class), snapshot.getKey(), false, snapshot.child("AuthUid").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}