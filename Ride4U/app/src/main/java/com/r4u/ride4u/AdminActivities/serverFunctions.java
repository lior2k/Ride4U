package com.r4u.ride4u.AdminActivities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONObject;

import java.util.Map;

public class serverFunctions {
    final private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    JSONObject jsonObject;

    public serverFunctions(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }


    private Task<Map<String,Object>> sendToServer2(String functionName) {
        return mFunctions.getHttpsCallable(functionName)
                .call(this.jsonObject)
                .continueWith(new Continuation<HttpsCallableResult, Map<String,Object>>() {
                    @Override
                    public Map<String,Object> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (Map<String,Object>) task.getResult().getData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
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

    public void sendNotification() {
        this.sendToServer("sendNotification");
    }

    public void removeUserFromDB() {
        this.sendToServer("removeUserFromDB");
    }

    public void messageNotification(){this.sendToServer("messageNotification");}

}
