package com.r4u.ride4u.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.r4u.ride4u.R;
import com.r4u.ride4u.Objects.User;

import java.util.ArrayList;

// This class is an adapter for displaying a list of users in a list view.
public class UsersAdapter extends ArrayAdapter<User> {


    public UsersAdapter(@NonNull Context context, int resource, ArrayList<User> users_list) {
        super(context, resource, users_list);
    }


    @Override
    public User getItem(int position) {
        return super.getItem(position);
    }

    @Override
    //   Returns a view for the specified position in the list view.
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_view, parent, false);

        TextView fullNameView = convertView.findViewById(R.id.fullname_text);
        String fullName = getContext().getString(R.string.fullName, user.getFirstname(), user.getLastname());
        fullNameView.setText(fullName);

        TextView idView = convertView.findViewById(R.id.id_text);
        String id = getContext().getString(R.string.id, user.getId());
        idView.setText(id);

        return convertView;
    }
}
