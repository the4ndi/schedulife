package com.iifratres.schedulife;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class User {
    /*
        Database file system
        users>userId>(FirstName,LastName,Email,PhoneNumber, Events)
     */

    public String userId;
    public int eventNumber;


    private String FirstName;
    public String LastName;
    public String Email;
    public String PhoneNumber;
    public String[] Events;


    DatabaseReference mDatabase;

    public User(String userId) {
        this.userId = userId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("data");
        this.updateUserInfo();
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    private void getFirstName(String uid) {

    }

    private void updateUserInfo() {

        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    FirstName = task.getResult().child("FirstName").toString();
                    LastName = task.getResult().child("LastName").toString();
                }
            }
        });
    }

    public String getEmail() {
        return this.Email;
    }
    private String getLastName() {
        return this.LastName;
    }

    private void setFirstName(String newName) {
        mDatabase.child("users").child(this.userId).child("FirstName").setValue(newName);
    }
    private void setLastName(String newName) {
        mDatabase.child("users").child(this.userId).child("LastName").setValue(newName);
    }

    public boolean AddEvent(String eventName, String eventDescription, String eventDate) {
        DatabaseReference usersRef = mDatabase.child("users").child(this.userId).child("Events");
        Map<String, User> Events = new HashMap<>();
        Events.put(eventName, new User(eventDate));
        return true;
    }

}
