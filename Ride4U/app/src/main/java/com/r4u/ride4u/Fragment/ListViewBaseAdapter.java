package com.r4u.ride4u.Fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.r4u.ride4u.Post;
import com.r4u.ride4u.R;

import java.util.ArrayList;

public class ListViewBaseAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Post> posts;

    public ListViewBaseAdapter(Context ctx, ArrayList<Post> posts) {
        context=ctx;
        this.posts = posts;
        layoutInflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.activity_postview,null);
        TextView textView = (TextView) convertView.findViewById(R.id.textview);
        Post p = posts.get(position);
        textView.setText("src: " + p.getSource() + "\ndest: " + p.getDestination() + "\nleaving at: 00:00");
        return convertView;
    }
}
