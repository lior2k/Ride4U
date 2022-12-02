package com.r4u.ride4u;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextInputLayout firstname = findViewById(R.id.firstname);
        final TextInputLayout lastname = findViewById(R.id.lastname);
        final TextInputLayout email = findViewById(R.id.email);
        final TextInputLayout id = findViewById(R.id.id);
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
                    if (firstnameETxt.getText() != null && lastnameETxt.getText() != null && emailETxt.getText() != null && idETxt.getText() != null && passwordETxt.getText() != null && confirmPasswordETxt.getText() != null) {
                        final String firstnameTxt = firstnameETxt.getText().toString();
                        final String lastnameTxt = lastnameETxt.getText().toString();
                        final String emailTxt = emailETxt.getText().toString();
                        final String idTxt = idETxt.getText().toString();
                        final String passwordTxt = passwordETxt.getText().toString();
                        final String confirmPasswordTxt = confirmPasswordETxt.getText().toString();

                        // check that passwords match
                        if (!passwordTxt.equals(confirmPasswordTxt)) {
                            Toast.makeText(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                        } else {

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
                                        databaseReference.child("users").child(idTxt).child("password").setValue(passwordTxt);
                                        Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    } else {
                        Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final Button backToLoginBtn = findViewById(R.id.backToLoginBtn);
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}