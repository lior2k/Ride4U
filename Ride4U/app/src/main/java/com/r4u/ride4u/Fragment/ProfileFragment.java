package com.r4u.ride4u.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.r4u.ride4u.AdminActivities.Operations;
import com.r4u.ride4u.UserActivities.ChangeAdress;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.R;

public class ProfileFragment extends Fragment {

    //    private String user_password;
    TextView profileName;
    TextView profileEmail;
    TextView changePassword;
    TextView changeAdress;
    Button adminBtn;

    // This function creates the screen using the data pulled from the functions below.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initProfile(view);
        setAdminBtn(view);
        return view;
    }

    // setting the admin options button
    // if the user viewing the profile page is an admin he'll see an admin options button
    private void setAdminBtn(View view) {
        adminBtn = view.findViewById(R.id.admin_options);
        if (!Login.user.getIsAdmin()) {
            adminBtn.setVisibility(View.INVISIBLE);
        } else {
            adminBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), Operations.class));
                }
            });
        }
    }

    private void setChangeBtns(View view) {
        changePassword = view.findViewById(R.id.change_pass_text);
        changeAdress = view.findViewById(R.id.adress_text2);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangeAdress.class));
            }
        });
        changeAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangeAdress.class));
            }
        });
    }
    // Initialize the profile's name and email acoording to the set database.

    private void initProfile(View view) {
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileName.setText(Login.user.getFullName());
        profileEmail.setText(Login.user.getEmail());
    }
}