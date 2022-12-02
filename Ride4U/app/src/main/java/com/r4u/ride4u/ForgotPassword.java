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

public class ForgotPassword extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setupSendNewPasswordButton();
        setupBackBtn();

    }

    private void setupSendNewPasswordButton() {
        final TextInputLayout email = findViewById(R.id.email);
        final TextInputLayout id = findViewById(R.id.id);
        final Button sendNewPWBtn = findViewById(R.id.sendNewPWBtn);
        sendNewPWBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final EditText emailETxt = email.getEditText();
                final EditText idETxt = id.getEditText();

                if (emailETxt != null && idETxt != null) {
                    if (emailETxt.getText() != null && idETxt.getText() != null) {
                        final String emailTxt = emailETxt.getText().toString();
                        final String idTxt = idETxt.getText().toString();

                        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // check that id exists in the system
                                if (snapshot.hasChild(idTxt)) {
                                    // check that email matches with existing email in database
                                    String dbEmail = snapshot.child(idTxt).child("email").getValue(String.class);
                                    if (emailTxt.equals(dbEmail)) {

                                        // send password to email.

                                        Toast.makeText(ForgotPassword.this, "New password sent to email", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ForgotPassword.this, "Email doesn't match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ForgotPassword.this, "ID not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });

                    } else {
                        Toast.makeText(ForgotPassword.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setupBackBtn() {
        final Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}