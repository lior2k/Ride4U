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
import com.r4u.ride4u.AdminActivities.ServerFunctions;
import com.r4u.ride4u.Fragment.Type;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();
    Type type; // corresponds the type of the adapter (home / active rides / history rides)
    ServerFunctions notificationSender;

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

        if (type == Type.Home) {
            if (post.getPublisherID().equals(Login.user.getId()) || post.getPassengerIDs().contains(Login.user.getId())) {
                convertView.findViewById(R.id.joinRideImgBtn).setVisibility(View.INVISIBLE);
            } else {
                setupJoinRideBtn(convertView, post);
            }
        } else if (type == Type.Active) {
            // if driver -> set google maps button
            if (post.getPublisherID().equals(Login.user.getId())) {
                convertView.findViewById(R.id.leaveButton).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.joinRideImgBtn).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.mapsButton).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.deleteIcon).setVisibility(View.VISIBLE);
                setupMapsBtn(convertView, post);
                setupDeleteIcon(convertView, post);
            } else {
                // if passenger -> set leave post button
                convertView.findViewById(R.id.leaveButton).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.joinRideImgBtn).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.mapsButton).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.deleteIcon).setVisibility(View.INVISIBLE);
                setupLeaveButton(convertView, post);
            }
        } else {
            convertView.findViewById(R.id.joinRideImgBtn).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.mapsButton).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.leaveButton).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.deleteIcon).setVisibility(View.INVISIBLE);
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
            personImage.setId(i + 1);

            RL.addView(personImage);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) personImage.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.postTextContent);
            if (i != 0) layoutParams.addRule(RelativeLayout.RIGHT_OF, i);

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
        String content = getContext().getString(R.string.postContent, post.getSource(), post.getDestination(), post.getLeavingDate(), post.getLeavingTime(), post.getAvailableSeats(), post.getSeats());
        postContent.setText(content);

        // set post price on top right
        TextView postCost = convertView.findViewById(R.id.costText);
        String cost = getContext().getString(R.string.costNIS, post.getCost());
        postCost.setText(cost);
    }

    //     Set the join ride button functionality.
    private void setupJoinRideBtn(View convertView, Post post) {
        ImageButton joinRideBtn = convertView.findViewById(R.id.joinRideImgBtn);

        joinRideBtn.setOnClickListener(v -> {
            // validate joining (user not already signed up for this ride / ride is not full / user is not the poster of the post)
            if (!post.addPassenger(Login.user.getId())) {
                Toast.makeText(getContext(), "Already part of this post", Toast.LENGTH_SHORT).show();
                return;
            }

            // user joined the ride - update database
            updateDB(post);

            // notify driver
            notifyDriver(post, true);

            Toast.makeText(getContext(), "Joined ride successfully!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateDB(Post post) {
        DatabaseReference postRoot = post.getSource().equals("Ariel") ? databaseReference.child("posts").child("fromAriel").child(post.getDestination()) : databaseReference.child("posts").child("toAriel").child(post.getSource());

        postRoot.child(post.getPostID()).child("availableSeats").setValue(post.getAvailableSeats());
        postRoot.child(post.getPostID()).child("passengerIDs").setValue(post.getPassengerIDs());
        if (post.isFull()) postRoot.child(post.getPostID()).child("full").setValue(true);
    }

    private void notifyDriver(Post post, boolean joined) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("publisherID", post.getPublisherID());
            jsonObject.put("username", Login.user.getFullName());
            notificationSender = new ServerFunctions(jsonObject);
            if (joined) {
                notificationSender.passengerJoinedNotification();
            } else {
                notificationSender.passengerLeftNotification();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setupMapsBtn(View convertView, Post post) {
        ImageButton mapsBtn = convertView.findViewById(R.id.mapsButton);
        mapsBtn.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) convertView.getContext();

            String src = post.getSource();
            String dest = post.getDestination();
            src = src.equals("Ariel") ? "Ariel University, Ramat HaGolan Street, Ari'el" : src + ", Israel";
            dest = dest.equals("Ariel") ? "Ariel University, Ramat HaGolan Street, Ari'el" : dest + ", Israel";

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

    private void setupLeaveButton(View convertView, Post post) {
        ImageButton leavePostBtn = convertView.findViewById(R.id.leaveButton);
        leavePostBtn.setOnClickListener(v -> {
            if (post.removeUser(Login.user.getId())) {
                DatabaseReference postRoot = post.getSource().equals("Ariel") ? databaseReference.child("posts").child("fromAriel").child(post.getDestination()) : databaseReference.child("posts").child("toAriel").child(post.getSource());

                HashMap<String, Object> updates = new HashMap<>();
                updates.put("availableSeats", post.getAvailableSeats());
                updates.put("passengerIDs", post.getPassengerIDs());
                updates.put("full", post.isFull());
                postRoot.child(post.getPostID()).updateChildren(updates);
                this.remove(post);
                this.notifyDataSetChanged();
                notifyDriver(post, false);
            }
        });
    }

    private void setupDeleteIcon(View convertView, Post post) {
        ImageButton deleteBtn = convertView.findViewById(R.id.deleteIcon);
        deleteBtn.setOnClickListener(v -> {

            sendDeleteNotificationToPassengers(post);

            deletePostFromDB(post);

        });
    }

    // notify all passengers that a driver has cancelled a drive
    private void sendDeleteNotificationToPassengers(Post post) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("publisherName", post.getPublisherFullName());
            jsonObject.put("ids", post.getPassengerIDs());
            ServerFunctions serverFunctions = new ServerFunctions(jsonObject);
            serverFunctions.postDeletedNotification();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deletePostFromDB(Post post) {
        DatabaseReference postRoot = post.getSource().equals("Ariel") ? databaseReference.child("posts").child("fromAriel").child(post.getDestination()) : databaseReference.child("posts").child("toAriel").child(post.getSource());
        postRoot.child(post.getPostID()).removeValue();
        this.remove(post);
        this.notifyDataSetChanged();
    }
}