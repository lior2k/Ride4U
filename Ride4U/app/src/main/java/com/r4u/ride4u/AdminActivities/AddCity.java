package com.r4u.ride4u.AdminActivities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
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

    MapView mapView;
    Geocoder geocoder;

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
        mapView = findViewById(R.id.city_map_view);
        geocoder = new Geocoder(this);
        setupSubmitButton();
    }

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
                        System.out.println(insertedCity);
                        System.out.println(insertedPrice);

//                        checkRealCity();
                        boolean cityExistsInDatabase = checkCityExistsInDatabase();
                        if(!cityExistsInDatabase) {
                            databaseReference.child("cities").child(insertedCity).setValue(insertedPrice);
                        }
                        else {
                            Toast.makeText(AddCity.this, "City already exists in database", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddCity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

//    private boolean checkRealCity() throws IOException {
//        // Search for the city using the Geocoder
//        List<Address> addresses = geocoder.getFromLocationName(insertedCity, 1,
//                37.4219999, -122.0862462, 37.4219999, -122.0862462);
//
//        // If the search returns a result, display the city on the map
//        if (addresses != null && addresses.size() > 0) {
//            Address address = addresses.get(0);
//            double latitude = address.getLatitude();
//            double longitude = address.getLongitude();
//
//            // Create a new LatLng object for the city's location
//            LatLng cityLocation = new LatLng(latitude, longitude);
//
//            // Add a marker to the map at the city's location
//            mapView.addMarker(new MarkerOptions().position(cityLocation));
//            mapView.add
//            // Move the map camera to the city's location
//            mapView.moveCamera(CameraUpdateFactory.newLatLng(cityLocation));
//        }
//    }

    private boolean checkCityExistsInDatabase() {
        flag = false;
        Task t = new Task
        databaseReference.child("cities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    if (insertedCity.equals(snapshot.getKey())) {
                        flag = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });

        return flag;
    }
}
