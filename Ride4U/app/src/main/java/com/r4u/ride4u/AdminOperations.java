package com.r4u.ride4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AdminOperations extends AppCompatActivity {

    ImageButton removeUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_operations);
        setRemoveUserBtn();
    }

    private void setRemoveUserBtn() {
        removeUserBtn = findViewById(R.id.blocking);
        removeUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminOperations.this, RemoveUser.class));
            }
        });
    }
}