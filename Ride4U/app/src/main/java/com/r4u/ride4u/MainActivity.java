package com.r4u.ride4u;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.r4u.ride4u.Fragment.HomeFragment;
import com.r4u.ride4u.Fragment.ProfileFragment;
import com.r4u.ride4u.Fragment.MyPostsFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupNavBar();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        // Log and toast
                        System.out.println("TOKEN: " + token);;

                    }
                });



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "homeTag").commit();
    }

    private void setupNavBar() {
        BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                selectedFragment = new HomeFragment();
                                break;
                            case R.id.nav_posts:
                                selectedFragment = new MyPostsFragment();
                                break;

                            case R.id.nav_add:
                                selectedFragment = null;
                                startActivity(new Intent(MainActivity.this, AddPost.class)); // start the add post activity
                                break;

                            case R.id.nav_profile:
//                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//                            editor.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                            editor.apply();
                                selectedFragment = new ProfileFragment();
                                break;
                        }

                        if (selectedFragment != null) {
                            if (selectedFragment instanceof HomeFragment) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, "homeTag").commit();
                            } else {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            }
                        }

                        return false;
                    }

                };
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
    }
}