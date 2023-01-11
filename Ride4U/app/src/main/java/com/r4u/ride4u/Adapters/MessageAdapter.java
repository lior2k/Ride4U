package com.r4u.ride4u.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.r4u.ride4u.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<String> {


    public MessageAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String message = getItem(position);

        if (message.startsWith("T")) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.send_msg_view, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recieve_msg_view, parent, false);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.gravity = Gravity.END;
//            LinearLayout msgLayout = convertView.findViewById(R.id.messageLayout);
//            msgLayout.setLayoutParams(params);
        }

        TextView messageView = convertView.findViewById(R.id.msgContent);
        messageView.setText(message.substring(2));

        return convertView;
    }

}
