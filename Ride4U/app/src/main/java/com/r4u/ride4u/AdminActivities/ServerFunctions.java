package com.r4u.ride4u.AdminActivities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONObject;

public class ServerFunctions {

    final private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    JSONObject jsonObject;

    public ServerFunctions(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    private Task<String> sendToServer(String functionName) {
        return mFunctions.getHttpsCallable(functionName)
                .call(this.jsonObject)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (String) task.getResult().getData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void passengerJoinedNotification() {
        this.sendToServer("sendNotification");
    }

    public void removeUserFromDB() {
        this.sendToServer("removeUserFromDB");
    }

    public void messageNotification() {
        this.sendToServer("messageNotification");
    }

    public void passengerLeftNotification() {
        this.sendToServer("LeaveNotification");
    }

    public void postDeletedNotification() {
        this.sendToServer("postDeletedNotification");
    }

}
