package com.r4u.ride4u;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputLayout id = findViewById(R.id.id);
        final TextInputLayout password = findViewById(R.id.password);

        final Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText idETxt = id.getEditText();
                final EditText passwordETxt = password.getEditText();

                // convert fields to string variables and check that they're not empty
                if (idETxt != null && passwordETxt != null) {
                    if (idETxt.getText() != null && passwordETxt.getText() != null) {
                        final String idTxt = idETxt.getText().toString();
                        final String passwordTxt = passwordETxt.getText().toString();

                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                // check if id exists in the system - we are using id as unique identifier for each user.
                                if (snapshot.hasChild(idTxt)) {
                                    // check if password entered matches with password in our database
                                    final String getPassword = snapshot.child(idTxt).child("password").getValue(String.class);
                                    if (passwordTxt.equals(getPassword)) {
                                        Toast.makeText(Login.this, "Successful log in", Toast.LENGTH_SHORT).show();
                                        // go to main screen
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Wrong ID", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        final Button forgotPWBtn = findViewById(R.id.forgotPWBtn);
        forgotPWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
    }



}