package com.r4u.ride4u.AdminActivities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONObject;

public class serverFunctions {
    final private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    JSONObject jsonObject;

    public serverFunctions(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Task<String> sendNotification() {
        return mFunctions.getHttpsCallable("sendNotification")
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

    public Task<String> removeUserFromDB() {

        return mFunctions.getHttpsCallable("removeUserFromDB")
                .call(this.jsonObject)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        return (String)task.getResult().getData();
                    }
                }).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d("print :",task.getResult());
                }
                else{
                    task.getException().printStackTrace();
                }
            }
        });
    }
}
