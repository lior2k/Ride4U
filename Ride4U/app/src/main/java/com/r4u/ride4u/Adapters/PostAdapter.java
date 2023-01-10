
package com.r4u.ride4u.Adapters;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.r4u.ride4u.AdminActivities.serverFunctions;
import com.r4u.ride4u.Fragment.Type;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    final private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    Type type; // corresponds the type of the adapter (home / active rides / history rides)

    static String val = "";

    public PostAdapter(Context ctx, int resource, List<Post> posts, Type type) {
        super(ctx, resource, posts);
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_postview, parent, false);

        setupPostText(convertView, post);

        addPersonsDrawings(convertView, post);

        // at home fragment we need to see and setup the join ride button, at my posts fragment we hide the join ride button
        if (type == Type.Home) {
            setupJoinRideBtn(convertView, post);
        } else if (type == Type.Active) {
            convertView.findViewById(R.id.joinRideImgBtn).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.mapsButton).setVisibility(View.VISIBLE);
            setupMapsBtn(convertView, post);
        } else {
            convertView.findViewById(R.id.joinRideImgBtn).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.mapsButton).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void removePreviousDrawings(RelativeLayout RL) {
        List<View> viewsToRemove = new ArrayList<>();
        for (int i = 0; i < RL.getChildCount(); i++) {
            View child = RL.getChildAt(i);
            if (child instanceof ImageView) {
                if ((int) child.getId() > 0 && (int) child.getId() < 6) {
                    viewsToRemove.add(child);
                }
            }
        }
        for (View view : viewsToRemove) {
            RL.removeView(view);
        }
    }

    // Dynamically draw person drawings on the post layout according to the amount of available seats and total seats.
    // empty person for available seats and filled person for a taken seat.
    private void addPersonsDrawings(View convertView, Post post) {
        RelativeLayout RL = convertView.findViewById(R.id.relativeLayout);

        removePreviousDrawings(RL);

        int persons = Integer.parseInt(post.getSeats());
        int unfilledPersons = Integer.parseInt(post.getAvailableSeats());
        for (int i = 0; i < persons; i++) {
            ImageView personImage = new ImageView(getContext());
            personImage.setId(i+1);

            RL.addView(personImage);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) personImage.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.postTextContent);
            if (i != 0)
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i);

            if (i >= (persons - unfilledPersons)) {
                personImage.setImageResource(R.drawable.ic_personempty);
            } else {
                personImage.setImageResource(R.drawable.ic_person);
            }

        }
    }

    // Set the post's title to be the driver's name and set the contents of the post to the relevant text.
    private void setupPostText(View convertView, Post post) {
        // post title = driver's name
        TextView posterName = convertView.findViewById(R.id.postPersonName);
        String fullName = getContext().getString(R.string.fullName, post.getPublisherFirstName(), post.getPublisherLastName());
        posterName.setText(fullName);

        // set post contents
        TextView postContent = convertView.findViewById(R.id.postTextContent);
        String content = getContext().getString(R.string.postContent, post.getSource(), post.getDestination(),
                post.getLeavingDate(),post.getLeavingTime(), post.getAvailableSeats(), post.getSeats());
        postContent.setText(content);

        // set post price on top right
        TextView postCost = convertView.findViewById(R.id.costText);
        String cost = getContext().getString(R.string.costNIS, post.getCost());
        postCost.setText(cost);
    }

//     Set the join ride button functionality.
    private void setupJoinRideBtn(View convertView, Post post) {
        ImageButton joinRideBtn = convertView.findViewById(R.id.joinRideImgBtn);
        joinRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate joining (user not already signed up for this ride / ride is not full / user is not the poster of the post)
                if (!post.addPassenger(Login.user.getId())) {
                    Toast.makeText(getContext(), "Already part of this post", Toast.LENGTH_SHORT).show();
                    return;
                }
                // user joined the ride - update database
                DatabaseReference postRoot = post.getSource().equals("Ariel") ?
                        databaseReference.child("posts").child("fromAriel").child(post.getDestination()) :
                        databaseReference.child("posts").child("toAriel").child(post.getSource());

                postRoot.child(post.getPostID()).child("availableSeats").setValue(post.getAvailableSeats());
                postRoot.child(post.getPostID()).child("passengerIDs").setValue(post.getPassengerIDs());
                if (post.isFull())
                    postRoot.child(post.getPostID()).child("full").setValue(true);

                // TODO notify driver

                Toast.makeText(getContext(), "Joined ride successfully!", Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();



                try {
                    jsonObject.put("publisherID", post.getPublisherID());
                    jsonObject.put("username", Login.user.getFullName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                serverFunctions notificationSender = new serverFunctions(jsonObject);
                notificationSender.sendNotification();

            }
        });
    }

    private void setupMapsBtn(View convertView, Post post) {
        ImageButton mapsBtn = convertView.findViewById(R.id.mapsButton);
        mapsBtn.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) convertView.getContext();


            String src = post.getSource();
            String dest = post.getDestination();
            src = src.equals("Ariel")? "Ariel University, Ramat HaGolan Street, Ari'el" : src + ", Israel";
            dest = dest.equals("Ariel")? "Ariel University, Ramat HaGolan Street, Ari'el" : dest + ", Israel";

            // Create a Uri from the source and destination locations
            Uri uri = Uri.parse("http://maps.google.com/maps?saddr=" + src + "&daddr=" + dest + "&mode=driving");
//            Uri uri = Uri.parse("google.navigation:q=" + dest);

            // Create an Intent with the action set to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);

            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

            // Attempt to start an activity that can handle the Intent
            activity.startActivity(mapIntent);
        });
    }

//    private Task<String> sendNotification(JSONObject data) {
//
//        return mFunctions.getHttpsCallable("sendNotification")
//                .call(data)
//                .continueWith(new Continuation<HttpsCallableResult, String>() {
//                    @Override
//                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
//                        return (String)task.getResult().getData();
//                    }
//                });
//    }

}