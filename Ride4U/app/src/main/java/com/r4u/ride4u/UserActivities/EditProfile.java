package com.r4u.ride4u.UserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r4u.ride4u.R;

public class EditProfile extends AppCompatActivity {

    private final FirebaseAuth authProfile = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    // Input
    private String emailTxt;
    private String adressTxt;
    private String oldPasswordText;
    private String newPasswordTxt;
    private String confirmPasswordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setupConfirmChangesBtn();
        setupBackToLoginBtn();
    }

    private void setupConfirmChangesBtn() {
        final TextInputLayout email = findViewById(R.id.email);
        final TextInputLayout adress = findViewById(R.id.adress);
        final TextInputLayout old_password = findViewById(R.id.old_password);
        final TextInputLayout new_password = findViewById(R.id.password);
        final TextInputLayout confirm_password = findViewById(R.id.confirmPassword);

        final Button confirm_changes = findViewById(R.id.confirmChanges);

        confirm_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText emailETxt = email.getEditText();
                final EditText adressETxt = adress.getEditText();
                final EditText old_passwordETxt = old_password.getEditText();
                final EditText new_passwordETxt = new_password.getEditText();
                final EditText confirm_passwordETxt = confirm_password.getEditText();

                if (emailETxt != null) {
                    if (adressETxt != null) {
                        if (adressETxt.getText().length() > 0) {
                            adressTxt = adressETxt.getText().toString();
                        }
                    }
                    if (old_passwordETxt != null && new_passwordETxt != null && confirm_passwordETxt != null) {
                        if (old_passwordETxt.getText().length() > 0 && new_passwordETxt.getText().length() > 0 && confirm_passwordETxt.getText().length() > 0) {
//                            if (authProfile.getCurrentUser().reauthenticate(authProfile.))
                        }
                    }
                }
            }
        });
    }

    private void setupBackToLoginBtn() {
        final TextView backToLoginBtn = findViewById(R.id.backToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }
}