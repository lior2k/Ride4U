package com.r4u.ride4u.AdminActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
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
    // Fields
    AutoCompleteTextView autoCompleteTextViewCity;
    ArrayAdapter<String> adapterCities;
    HashMap<String, String> citiesAndPrices;
    ArrayList<String> citiesList;
    // Buttons
    Button submitButton;
    TextView currentPrice;
    // Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    String chosenCity;
    String newPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_prices);

        citiesAndPrices = new HashMap<>();
        citiesList = new ArrayList<>();

        initCitiesAndPrices();
        setupCityAutoComplete();
        setupCurrentPrice();
        setupNewPrice();
        setupSubmitButton();
        setupBackButtonListener();
    }

    /**
     * Sets up the behavior of the submit button in the ChangePrices activity.
     * When the submit button is clicked, the function checks if the city and new price fields have been filled out.
     * If both fields have been filled out, the function checks if the chosen city is "Ariel".
     * If the chosen city is "Ariel", a toast message is displayed.
     * If the chosen city is not "Ariel", the function updates the price in the database.
     * If either of the fields has not been filled out, a toast message is displayed.
     */
    private void setupSubmitButton() {
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            final TextInputLayout newPriceView = findViewById(R.id.new_price);
            final EditText newPriceTxt = newPriceView.getEditText();
            if (newPriceTxt != null) {
                if (autoCompleteTextViewCity.getText().length() > 0 && newPriceTxt.getText().length() > 0) {

                    chosenCity = autoCompleteTextViewCity.getText().toString();
                    newPrice = newPriceTxt.getText().toString();

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

    /**
     * Sets a new price for a city that exists in the database
     * Show a toast message if a failure occures.
     */
    private void ChangePriceInDataBase() {
        databaseReference.child("cities").child(chosenCity).setValue(newPrice)
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Changing price failed!! Please try again later", Toast.LENGTH_LONG).show());
    }

    /**
     * Sets the new price to be shows on screen.
     */
    private void setupNewPrice() {
        final TextInputLayout newPriceView = findViewById(R.id.new_price);
        final EditText newPriceTxt = newPriceView.getEditText();
        if (newPriceTxt != null) {
            if (newPriceTxt.getText().length() > 0) {
                newPrice = newPriceTxt.getText().toString();
            }
        }
    }

    /**
     * Updates an existing city price.
     */
    private void setupCurrentPrice() {
        currentPrice = findViewById(R.id.current_price);
        autoCompleteTextViewCity.setOnItemClickListener((parent, view, position, id) -> {
            String city = (String) parent.getAdapter().getItem(position);
            currentPrice.setText(citiesAndPrices.get(city));
        });
    }

    /**
     * Initializes the cities and prices data structures.
     * Creates a map and a list for storing the cities and their corresponding prices.
     * Populates the map and list with data from the database by using a value event listener.
     */
    private void initCitiesAndPrices() {
        databaseReference.child("cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                citiesAndPrices.clear();
                citiesList.clear();
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    citiesAndPrices.put(snapshot.getKey(), snapshot.getValue(String.class));
                    citiesList.add(snapshot.getKey());
                }
                adapterCities.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    /**
     * Sets up that list showed on the list toggle button from the "cities" database.
     */
    private void setupCityAutoComplete() {
        autoCompleteTextViewCity = findViewById(R.id.city);
        adapterCities = new ArrayAdapter<>(this, R.layout.cities_list, citiesList);
        autoCompleteTextViewCity.setAdapter(adapterCities);
    }

    private void setupBackButtonListener() {
        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}












