package com.r4u.ride4u.UserActivities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.r4u.ride4u.R;

import java.util.Objects;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    private FirebaseAuth authProfile;
    private String firstnameTxt;
    private String lastnameTxt;
    private String emailTxt;
    private String idTxt;
    private String passwordTxt;
    private String confirmPasswordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupSignUpButton();
        setupBackToLoginBtn();

    }

    private void setupSignUpButton() {
        final TextInputLayout firstname = findViewById(R.id.firstname);
        final TextInputLayout lastname = findViewById(R.id.lastname);
        final TextInputLayout email = findViewById(R.id.email);
        final TextInputLayout id = findViewById(R.id.history_);
        final TextInputLayout password = findViewById(R.id.password);
        final TextInputLayout confirmPassword = findViewById(R.id.confirmPassword);

        final Button signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText firstnameETxt = firstname.getEditText();
                final EditText lastnameETxt = lastname.getEditText();
                final EditText emailETxt = email.getEditText();
                final EditText idETxt = id.getEditText();
                final EditText passwordETxt = password.getEditText();
                final EditText confirmPasswordETxt = confirmPassword.getEditText();

                // convert fields to string variables and check that they're not empty
                if (firstnameETxt != null && lastnameETxt != null && emailETxt != null && idETxt != null && passwordETxt != null && confirmPasswordETxt != null) {
                    if (firstnameETxt.getText().length() > 0 && lastnameETxt.getText().length() > 0 && emailETxt.getText().length() > 0 && idETxt.getText().length() > 0 && passwordETxt.getText().length() > 0 && confirmPasswordETxt.getText().length() > 0) {
                        firstnameTxt = firstnameETxt.getText().toString();
                        lastnameTxt = lastnameETxt.getText().toString();
                        emailTxt = emailETxt.getText().toString();
                        idTxt = idETxt.getText().toString();
                        passwordTxt = passwordETxt.getText().toString();
                        confirmPasswordTxt = confirmPasswordETxt.getText().toString();

                        // check that passwords match
                        if (!passwordTxt.equals(confirmPasswordTxt)) {
                            Toast.makeText(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                        } else {
                            authRegistration();
                        }
                    } else {
                        Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setupBackToLoginBtn() {
        final TextView backToLoginBtn = findViewById(R.id.backToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void authRegistration() {
        authProfile = FirebaseAuth.getInstance();
        authProfile.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkAdmin();
                            realTimeRegistration();
                            Toast.makeText(getApplicationContext(),"Registration successful!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            // Registration failed
                            String message = Objects.requireNonNull(task.getException()).getLocalizedMessage();
                            if (message != null && message.startsWith("The given password is invalid")) {
                                Toast.makeText(getApplicationContext(),"password is invalid, it should be at least 6 characters", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void realTimeRegistration() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //check if id is already registered
                if (snapshot.hasChild(idTxt)) {
                    Toast.makeText(Register.this, "ID is already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // sending data to realtime firebase database
                    databaseReference.child("users").child(idTxt).child("firstname").setValue(firstnameTxt);
                    databaseReference.child("users").child(idTxt).child("lastname").setValue(lastnameTxt);
                    databaseReference.child("users").child(idTxt).child("email").setValue(emailTxt);
                    databaseReference.child("users").child(idTxt).child("devicetoken").setValue(String.valueOf(FirebaseMessaging.getInstance().getToken()));
                    FirebaseUser currentUser = authProfile.getCurrentUser();
                    if (currentUser != null) {
                        databaseReference.child("users").child(idTxt).child("AuthUid").setValue(currentUser.getUid());
                    }

//                    String uID = "";
//                    FirebaseUser currentUser = authProfile.getCurrentUser();
//                    if (currentUser != null) {
//                        uID = currentUser.getUid();
//                    }
//                    User newUser = new User(firstnameTxt, lastnameTxt, emailTxt, idTxt, false, uID);
//                    databaseReference.child("users").child(idTxt).setValue(newUser);
//                    databaseReference.child("users").child(idTxt).child("userId").removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }
        });
    }

    private void checkAdmin() {
        databaseReference.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                databaseReference.child("users").child(idTxt).child("isAdmin").setValue("false");
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    if (idTxt.equals(snapshot.getKey())) {
                        databaseReference.child("users").child(idTxt).child("isAdmin").setValue("true");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }
        });
    }


}
