package com.r4u.ride4u.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.r4u.ride4u.Post;
import com.r4u.ride4u.R;

import java.util.List;


public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(Context ctx, int resource, List<Post> posts) {
        super(ctx, resource, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post p = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_postview, parent, false);

        TextView textView = (TextView) convertView.findViewById(R.id.textview);
        textView.setText("Leaving from: " + p.getSource() + "\nGoing to: " + p.getDestination() + "\nLeaving at: 00:00");
        return convertView;
    }
}
