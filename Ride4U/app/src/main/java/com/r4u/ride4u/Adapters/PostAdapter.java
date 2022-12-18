package com.r4u.ride4u.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();

    public PostAdapter(Context ctx, int resource, List<Post> posts) {
        super(ctx, resource, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_postview, parent, false);
        setupPostText(convertView, post);

        addPersonsDrawings(convertView, post);

        setupJoinRideBtn(convertView, post);

        return convertView;
    }

    // Dynamically draw person drawings on the post layout according to the amount of available seats and total seats.
    private void addPersonsDrawings(View convertView, Post post) {
        RelativeLayout RL = convertView.findViewById(R.id.relativeLayout);
        int persons = Integer.parseInt(post.getSeats());
        int unfilledPersons = Integer.parseInt(post.getAvailableSeats());
        for (int i = 0; i < persons; i++) {
            ImageView personImage = new ImageView(getContext());
            personImage.setId(i+1);
            if (i >= (persons - unfilledPersons)) {
                personImage.setImageResource(R.drawable.ic_personempty);
            } else {
                personImage.setImageResource(R.drawable.ic_person);
            }
            RL.addView(personImage);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) personImage.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.postTextContent);
            if (i != 0)
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i);

        }
    }

    // Set the post's title to be the driver's name and set the contents of the post to the relevant text.
    private void setupPostText(View convertView, Post post) {
        // post title = driver's name
        TextView posterName = convertView.findViewById(R.id.postPersonName);
        String fullName = getContext().getString(R.string.fullName, post.getPublisherFirstName(), post.getPublisherLastName());
        posterName.setText(fullName);

        // post contents
        TextView postContent = convertView.findViewById(R.id.postTextContent);
        String content = getContext().getString(R.string.postContent, post.getSource(), post.getDestination(),
                post.getLeavingDate(),post.getLeavingTime(), post.getAvailableSeats(), post.getSeats());
        postContent.setText(content);

        // post cost
        TextView postCost = convertView.findViewById(R.id.costText);
        String cost = getContext().getString(R.string.costNIS, post.getCost());
        postCost.setText(cost);
    }

    // Set the join ride button functionality.
    private void setupJoinRideBtn(View convertView, Post post) {
        ImageButton joinRideBtn = convertView.findViewById(R.id.joinRideImgBtn);
        joinRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate joining (user not already signed up for this ride / ride is not full / user is not the poster of the post)
                if (!post.addPassenger(Login.user.getId())) {
                    Toast.makeText(getContext(), "Failed to join ride", Toast.LENGTH_SHORT).show();
                    return;
                }
                // user joined the ride - update database
                databaseReference.child("posts").child(post.getPostID()).child("availableSeats").setValue(post.getAvailableSeats());
                databaseReference.child("posts").child(post.getPostID()).child("passengerIDs").setValue(post.getPassengerIDs());
                if (post.isFull())
                    databaseReference.child("posts").child(post.getPostID()).child("full").setValue(true);

                // TODO notify driver

                // TODO add to my rides
//                Login.user.getRideHistory().add(post);
//                databaseReference.child("users").child(Login.user.getId()).child("rideHistory").setValue(Login.user.getRideHistory());

                Toast.makeText(getContext(), "Joined ride successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}