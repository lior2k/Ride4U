package com.r4u.ride4u.UserActivities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.r4u.ride4u.Fragment.AddPostFragment;
import com.r4u.ride4u.Fragment.HomeFragment;
import com.r4u.ride4u.Fragment.ProfileFragment;
import com.r4u.ride4u.Fragment.MyPostsFragment;
import com.r4u.ride4u.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupNavBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private void setupNavBar() {
        BottomNavigationView.OnItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                item.setChecked(true);
                                selectedFragment = new HomeFragment();
                                break;

                            case R.id.nav_posts:
                                item.setChecked(true);
                                selectedFragment = new MyPostsFragment();
                                break;

                            case R.id.nav_add:
                                item.setChecked(true);
                                selectedFragment = new AddPostFragment();
//                                startActivity(new Intent(MainActivity.this, AddPost.class)); // start the add post activity
                                break;

                            case R.id.nav_profile:
                                item.setChecked(true);
                                selectedFragment = new ProfileFragment();
                                break;
                        }

                        if (selectedFragment != null) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                        }
                        return false;
                    }

                };
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
    }

}