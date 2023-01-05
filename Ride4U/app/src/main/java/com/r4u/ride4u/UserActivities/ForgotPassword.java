package com.r4u.ride4u.UserActivities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.R;

public class ForgotPassword extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    String emailTxt;
    String idTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        setupSendNewPasswordButton();
        setupBackBtn();
    }

    /**
     Set up the button for sending a new password.
     Set an OnClickListener for the sendNewPWBtn Button when clicked, set the emailTxt
     and idTxt variables to the text in the email and id.
     */
    private void setupSendNewPasswordButton() {
        final TextInputLayout email = findViewById(R.id.email);
        final TextInputLayout id = findViewById(R.id.history_);
        final Button sendNewPWBtn = findViewById(R.id.sendNewPWBtn);
        sendNewPWBtn.setOnClickListener(view -> {
            final EditText emailETxt = email.getEditText();
            final EditText idETxt = id.getEditText();

            if (emailETxt != null && idETxt != null) {
                if (emailETxt.getText().length() > 0 && idETxt.getText().length() > 0) {
                    emailTxt = emailETxt.getText().toString();
                    idTxt = idETxt.getText().toString();

                    // Check the email and id on the database.
                    checkIdAndEmailOnDatabase();

                } else {
                    Toast.makeText(ForgotPassword.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     Check the email and ID on the database to see if they match an existing user.
     If a match is found, reset the user's password.
     */
    private void checkIdAndEmailOnDatabase() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check that id exists in the system
                if (snapshot.hasChild(idTxt)) {
                    // check that email matches with existing email in database
                    String dbEmail = snapshot.child(idTxt).child("email").getValue(String.class);
                    if (emailTxt.equals(dbEmail)) {
                        resetPassword();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Email doesn't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, "ID not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }

        });
    }

    private void setupBackBtn() {
        final TextView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> finish());
    }

    /**
     Reset the user's password by sending a password reset email to the user's email address.
     */
    private void resetPassword() {
        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(emailTxt).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(ForgotPassword.this, "please check your inbox for password reset link", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}