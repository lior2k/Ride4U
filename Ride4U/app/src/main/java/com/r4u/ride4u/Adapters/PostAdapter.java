package com.r4u.ride4u.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r4u.ride4u.FCMSend;
import com.r4u.ride4u.Login;
import com.r4u.ride4u.Post;
import com.r4u.ride4u.R;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    ImageButton joinRideBtn;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance(Login.firebase_url).getReference();

    public PostAdapter(Context ctx, int resource, List<Post> posts) {
        super(ctx, resource, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_postview, parent, false);

        TextView posterName = convertView.findViewById(R.id.postPersonName);
        String fullName = getContext().getString(R.string.fullName, post.getPublisherFirstName(), post.getPublisherLastName());
        posterName.setText(fullName);

        TextView postContent = convertView.findViewById(R.id.postTextContent);
        String content = getContext().getString(R.string.postContent, post.getSource(), post.getDestination(),
                post.getLeavingDate(),post.getLeavingTime(), post.getAvailableSeats(), post.getSeats());
        postContent.setText(content);

        joinRideBtn = convertView.findViewById(R.id.joinRideImgBtn);
        setupJoinRideBtn(post);

        return convertView;
    }

    private void setupJoinRideBtn(Post post) {
        joinRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate joining (user not already signed up for this ride and ride is not full)
                if (!post.addPassenger(Login.user.getId())) {
                    return;
                }
                // user joined the ride - update database
                databaseReference.child("posts").child(post.getPostID()).child("availableSeats").setValue(post.getAvailableSeats());
                databaseReference.child("posts").child(post.getPostID()).child("passengerIDs").setValue(post.getPassengerIDs());

                // notify driver
                String title = "New Passenger!";
                String msg = Login.user.getFirstname() + Login.user.getLastname() + "joined your ride from " + post.getSource() + "to " + post.getDestination() + "at " + post.getLeavingTime();
                FCMSend.pushNotification(getContext(),
                        "eKS5oCafSQi3IFeH4zHN_M:APA91bFAFKXAuSSByfLMjj-AWlBqF4rWSrIW-B4_DvXmp2EAwTWN5oL-5kOBWxhMwnmV9jtMdAZmO2_la-W7q5NQ5qkvK2Re7WzJth6pRb1r2NXXS6JjYjd5Xr2HLAN61NpUsFh3SYOM",
                        title,
                        msg);

                // add to my rides

            }
        });
    }


}
