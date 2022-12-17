package com.r4u.ride4u.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.r4u.ride4u.Objects.Post;
import com.r4u.ride4u.R;
import java.util.List;

public class RideAdapter extends ArrayAdapter<Post> {


    public RideAdapter(Context ctx, int resource, List<Post> posts) {
        super(ctx, resource, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Post post = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_postview, parent, false);

        convertView.findViewById(R.id.joinRideImgBtn).setVisibility(View.INVISIBLE);

        setupPostText(convertView, post);

        addPersonsDrawings(convertView, post);

        return convertView;
    }

    private void addPersonsDrawings(View convertView, Post post) {
        RelativeLayout RL = convertView.findViewById(R.id.relativeLayout);
        int persons = Integer.parseInt(post.getSeats());
        int unfilledPersons = Integer.parseInt(post.getAvailableSeats());
        for (int i = 0; i < persons; i++) {
            ImageView per = new ImageView(getContext());
            per.setId(i + 1);
            if (i >= (persons - unfilledPersons)) {
                per.setImageResource(R.drawable.ic_personempty);
            } else {
                per.setImageResource(R.drawable.ic_person);
            }
            RL.addView(per);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) per.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, R.id.postTextContent);
            if (i != 0)
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i);
        }
    }


    private void setupPostText(View convertView, Post post) {
        TextView posterName = convertView.findViewById(R.id.postPersonName);
        String fullName = getContext().getString(R.string.fullName, post.getPublisherFirstName(), post.getPublisherLastName());
        posterName.setText(fullName);

        TextView postContent = convertView.findViewById(R.id.postTextContent);
        String content = getContext().getString(R.string.postContent, post.getSource(), post.getDestination(),
                post.getLeavingDate(), post.getLeavingTime(), post.getAvailableSeats(), post.getSeats());
        postContent.setText(content);

        TextView postCost = convertView.findViewById(R.id.costText);
        String cost = getContext().getString(R.string.costNIS, post.getCost());
        postCost.setText(cost);
    }
}