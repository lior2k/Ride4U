package com.r4u.ride4u.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.r4u.ride4u.R;
import com.r4u.ride4u.User;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {

    public UsersAdapter(@NonNull Context context, int resource, ArrayList<User> users_list) {super(context, resource, users_list);}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_view, parent, false);

        TextView fullNameView = (TextView) convertView.findViewById(R.id.fullname_text);
        String fullName = getContext().getString(R.string.fullName, user.getFirstname() , user.getLastname());
        fullNameView.setText(fullName);

        TextView idView = (TextView) convertView.findViewById(R.id.id_text);
        String id = getContext().getString(R.string.id, user.getId());
        idView.setText(id);

        return convertView;
    }
}
