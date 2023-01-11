package com.r4u.ride4u.AdminActivities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.R;
import com.r4u.ride4u.UserActivities.Login;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AddCity extends AppCompatActivity {

    TextInputLayout cityInput;
    TextInputLayout priceInput;

    Button submitBtn;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    String insertedCity;
    String insertedPrice;

    // flag true if city exists in database or false otherwise.
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        cityInput = findViewById(R.id.city_input);
        priceInput = findViewById(R.id.set_price);
        setupBackButtonListener();
        setupSubmitButton();
    }

    /**
     * Sets up the behavior of the submit button in the AddCity activity.
     * When the submit button is clicked, the function checks if the city and price fields have been filled out.
     * If both fields have been filled out, the function checks if the city already exists in the database.
     * If the city does not exist in the database, the function adds the city and price to the database and closes the activity.
     * If the city does exist in the database, a toast message is displayed.
     * If either of the fields has not been filled out, a toast message is displayed.
     */
    public void setupSubmitButton() {
        submitBtn = findViewById(R.id.submit_button_c);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText cityText = cityInput.getEditText();
                final EditText priceText = priceInput.getEditText();
                if (cityText != null && priceText != null) {
                    if (cityText.getText().length() > 0 && priceText.getText().length() > 0) {
                        insertedCity = cityText.getText().toString();
                        insertedPrice = priceText.getText().toString();

                        checkCityExistsInDatabase();

                    }
                    else {
                        Toast.makeText(AddCity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Checks if a city already exists in the database
     * @return true if the city exists or false if it isnt.
     */
    private void checkCityExistsInDatabase() {
        flag = false;
        databaseReference.child("cities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    if (insertedCity.equals(snapshot.getKey())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    databaseReference.child("cities").child(insertedCity).setValue(insertedPrice);
                    finish();
                } else {
                    Toast.makeText(AddCity.this, "City already exists in database", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void setupBackButtonListener() {
        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}
