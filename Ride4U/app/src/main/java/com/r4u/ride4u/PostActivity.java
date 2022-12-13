package com.r4u.ride4u;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r4u.ride4u.Fragment.HomeFragment;

public class PostActivity extends AppCompatActivity {

    Post post;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getPost();
        setValue();

        setupJoinRideButton();
    }

    private void getPost() {
        Intent previousIntent = getIntent();
        String parsedStringID = previousIntent.getStringExtra("index");
        post = HomeFragment.posts.get(Integer.parseInt(parsedStringID));
    }

    private void setValue() {
        TextView textView = findViewById(R.id.textview);
        textView.setText("Leaving from: " + post.getSource() + "\nGoing to: " + post.getDestination() + "\nLeaving at: "+post.getLeavingTime()
                +"\nSeats: "+post.getSeats()+"\nPosted By: "+post.getPublisherFirstName()+" "+post.getPublisherLastName());
    }

    private void setupJoinRideButton() {
        Button joinRide = findViewById(R.id.joinRideBtn);
        joinRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSeats = post.decrementSeats();
                databaseReference.child("posts").child(post.getPublisherID()).child("seats").setValue(newSeats);

                // notify driver


                finish();
            }
        });
    }

}