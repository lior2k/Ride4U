package com.r4u.ride4u.UserActivities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.R;
import com.r4u.ride4u.Objects.User;

public class Login extends AppCompatActivity {
    FirebaseAuth autoProfile;
    public static User user;
    public static String firebase_url = "https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(firebase_url).getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupLoginButton();
        setupRegisterButton();
        setupForgotPasswordButton();

    }

    private void setupLoginButton() {
        final TextInputLayout email = findViewById(R.id.email);
        final TextInputLayout password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText emailETxt = email.getEditText();
                final EditText passwordETxt = password.getEditText();

                // convert fields to string variables and check that they're not empty
                if (emailETxt != null && passwordETxt != null) {
                    if (emailETxt.getText() != null && passwordETxt.getText() != null) {
                        final String emailTxt = emailETxt.getText().toString();
                        final String passwordTxt = passwordETxt.getText().toString();

                        //login with authentication
                        autoProfile = FirebaseAuth.getInstance();
                        autoProfile.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    getIdByEmail(emailTxt);
                                    Toast.makeText(getApplicationContext(),"Login successful!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                }
                                else {
                                    // Login failed
                                    Toast.makeText(getApplicationContext(),"Login failed!! Please try again later", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // go to register activity
    private void setupRegisterButton() {
        final Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(view -> startActivity(new Intent(Login.this, Register.class)));
    }

    // go to reset password activity
    private void setupForgotPasswordButton() {
        final Button forgotPWBtn = findViewById(R.id.forgotPWBtn);
        forgotPWBtn.setOnClickListener(view -> startActivity(new Intent(Login.this, ForgotPassword.class)));
    }

    // get the email stored at realtime database to compare with the email inserted.
    private void getIdByEmail(String email) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    if (email.equals(snapshot.child("email").getValue(String.class))) {
                        String userID = snapshot.getKey();
                        String firstName = snapshot.child("firstname").getValue(String.class);
                        String lastName = snapshot.child("lastname").getValue(String.class);
                        String Uid = snapshot.child("AuthUid").getValue(String.class);
                        String isAdminStr = snapshot.child("isAdmin").getValue(String.class);
                        Boolean isAdmin = false;
                        if (isAdminStr != null) {
                            isAdmin = str_to_boolean(isAdminStr);
                        }
                        user = new User(firstName, lastName, email, userID, isAdmin, Uid);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private Boolean str_to_boolean(String isAdmin) {
        return isAdmin.equals("true");
    }
}