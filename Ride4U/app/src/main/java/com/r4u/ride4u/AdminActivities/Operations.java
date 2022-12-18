package com.r4u.ride4u.AdminActivities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.r4u.ride4u.R;

public class Operations extends AppCompatActivity {

    ImageButton removeUserBtn;
    ImageButton pricesBtn;
    ImageButton addCityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_operations);
        setupRemoveUserBtn();
        setupPricesBtn();
        setupAddCityBtn();
    }

    private void setupPricesBtn() {
        pricesBtn = findViewById(R.id.money);
        pricesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Operations.this, ChangePrices.class));
            }
        });
    }

    private void setupRemoveUserBtn() {
        removeUserBtn = findViewById(R.id.blocking);
        removeUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Operations.this, RemoveUser.class));
            }
        });
    }

    private void setupAddCityBtn() {
        addCityBtn = findViewById(R.id.add_city);
        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Operations.this, AddCity.class));
            }
        });
    }
}