package com.r4u.ride4u.UserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.r4u.ride4u.R;

public class ChangePassword extends AppCompatActivity {

    FirebaseAuth autoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);
    }
}