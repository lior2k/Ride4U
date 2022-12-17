package com.r4u.ride4u.AdminActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangePrices extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextViewCity;
    ArrayAdapter<String> adapterCities;
    HashMap<String, String> citiesAndPrices;
    ArrayList<String> citiesList;

    Button submitButton;
    TextView currentPrice;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    String chosenCity;
    String newPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_prices);

        initCitiesAndPrices();
        setupCityAutoComplete();
        setupCurrentPrice();
        setupNewPrice();
        setupSubmitButton();
    }

    private void setupSubmitButton() {
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            final TextInputLayout newPriceView = findViewById(R.id.new_price);
            final EditText newPriceTxt = newPriceView.getEditText();
            if (newPriceTxt != null) {
                if (autoCompleteTextViewCity.getText().length() > 0 && newPriceTxt.getText().length() > 0) {

                    chosenCity = autoCompleteTextViewCity.getText().toString();
                    newPrice = newPriceTxt.getText().toString();
                    System.out.println(chosenCity);
                    System.out.println(newPrice);

                    if (chosenCity.equals("Ariel")) {
                        Toast.makeText(ChangePrices.this, "Ariel price cannot change", Toast.LENGTH_SHORT).show();
                    } else {
                        ChangePriceInDataBase();
                    }
                }
                else {
                    Toast.makeText(ChangePrices.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ChangePriceInDataBase() {
        databaseReference.child("cities").child(chosenCity).setValue(newPrice)
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Changing price failed!! Please try again later", Toast.LENGTH_LONG).show());
    }

    private void setupNewPrice() {
        final TextInputLayout newPriceView = findViewById(R.id.new_price);
        final EditText newPriceTxt = newPriceView.getEditText();
        if (newPriceTxt != null) {
            if (newPriceTxt.getText().length() > 0) {
                newPrice = newPriceTxt.getText().toString();
            }
        }
    }

    private void setupCurrentPrice() {
        currentPrice = findViewById(R.id.current_price);
        autoCompleteTextViewCity.setOnItemClickListener((parent, view, position, id) -> {
            String city = (String) parent.getAdapter().getItem(position);
            currentPrice.setText(citiesAndPrices.get(city));
        });
    }

    private void initCitiesAndPrices() {
        citiesAndPrices = new HashMap<>();
        citiesList = new ArrayList<>();
        databaseReference.child("cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    citiesAndPrices.put(snapshot.getKey(), snapshot.getValue(String.class));
                    citiesList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void setupCityAutoComplete() {
        autoCompleteTextViewCity = findViewById(R.id.city);
        adapterCities = new ArrayAdapter<>(this, R.layout.cities_list, citiesList);
        autoCompleteTextViewCity.setAdapter(adapterCities);
    }
}












