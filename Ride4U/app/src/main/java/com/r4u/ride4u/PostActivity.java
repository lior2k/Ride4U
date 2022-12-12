package com.r4u.ride4u;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.r4u.ride4u.Fragment.HomeFragment;

public class PostActivity extends AppCompatActivity {

    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getPost();
        setValue();
    }

    private void getPost() {
        Intent previousIntent = getIntent();
        String parsedStringID = previousIntent.getStringExtra("index");
        post = HomeFragment.posts.get(Integer.parseInt(parsedStringID));
    }

    private void setValue() {
        TextView textView = (TextView) findViewById(R.id.textview);
        textView.setText("Leaving from: " + post.getSource() + "\nGoing to: " + post.getDestination() + "\nLeaving at: "+post.getLeavingTime()
                +"\nSeats: "+post.getSeats()+"\nPosted By: "+post.getPublisherFirstName()+" "+post.getPublisherLastName());
    }
}