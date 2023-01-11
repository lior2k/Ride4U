package com.r4u.ride4u.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Adapters.DateAndTimeFormat;
import com.r4u.ride4u.Adapters.MessageAdapter;
import com.r4u.ride4u.AdminActivities.serverFunctions;
import com.r4u.ride4u.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {

    DatabaseReference firebaseSelfRoot;
    DatabaseReference firebaseOtherRoot;
    String name;
    String id;
    TextView messageView;
    ListView listView;
    MessageAdapter messageAdapter;
    private ArrayList<String> messages;

    private boolean setup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        messages = new ArrayList<>();
        listView = findViewById(R.id.messagesList);
        messageView = findViewById(R.id.messageInput);
        TextView username = findViewById(R.id.nameTitle);
        name = getIntent().getExtras().getString("userName");
        id = getIntent().getExtras().getString("userId");
        username.setText(name);
        firebaseSelfRoot = FirebaseDatabase.getInstance(Login.firebase_url).getReference().child("users").child(Login.user.getId()).child("messages").child(id);
        firebaseOtherRoot = FirebaseDatabase.getInstance(Login.firebase_url).getReference().child("users").child(id).child("messages").child(Login.user.getId());

        initMsgList();
        setupBackButtonListener();
        setupSendBtn();

    }

    private void initMsgList() {
        firebaseSelfRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {

                messages.clear();
                for (DataSnapshot data : dataSnapShot.getChildren()) {
                    String message = data.getValue(String.class);
                    messages.add(message);
                }

                if (!setup) {
                    setupListView();
                    setup = true;
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getCode());
            }

        });
    }

    private void setupSendBtn() {
        ImageView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(v -> {
            String message = messageView.getText().toString();
            if (message.length() > 0) {
                DatabaseReference selfRef = firebaseSelfRoot.push();
                DatabaseReference otherRef = firebaseOtherRoot.push();
                selfRef.setValue("T:"+message);
                otherRef.setValue("F:"+message);
                messageView.setText("");
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("publisherID", id);
                    jsonObject.put("username", Login.user.getFullName());
                    jsonObject.put("content", message);
                    serverFunctions notificationSender = new serverFunctions(jsonObject);
                    notificationSender.messageNotification();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
    }

    private void setupListView() {
        messageAdapter = new MessageAdapter(this, 0, messages);
        listView.setAdapter(messageAdapter);
    }

    private void setupBackButtonListener() {
        ImageButton backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

}