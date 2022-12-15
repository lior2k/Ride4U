package com.r4u.ride4u.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.AdminOperations;
import com.r4u.ride4u.Login;
import com.r4u.ride4u.PostActivity;
import com.r4u.ride4u.R;
import com.r4u.ride4u.Register;

public class ProfileFragment extends Fragment {

    //    private String user_password;
    TextView profileName;
    TextView profileEmail;
    Button adminBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        initProfile();
        setAdminBtn(view);
        return view;
    }

    private void setAdminBtn(View view) {
        adminBtn = view.findViewById(R.id.admin_options);
        if (!Login.user.getIsAdmin()) {
            adminBtn.setVisibility(View.INVISIBLE);
        } else {
            adminBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), AdminOperations.class));
                }
            });
        }
    }

    private void initProfile() {
        profileName.setText(Login.user.getFirstname() + " " + Login.user.getLastname());
        profileEmail.setText(Login.user.getEmail());
    }

}