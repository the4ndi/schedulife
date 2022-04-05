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
    String userId;
    String eventNumber;
    String Name;
    String email;
    String PhoneNumber;
    String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(String eventNumber) {
        this.eventNumber = eventNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    public String getName() {
        return this.Name;
    }

    public void setName(String newName) {
        mDatabase.child("users").child(this.userId).child("Name").setValue(newName);
    }

    /*
            Database file system
            users>userId>(FirstName,LastName,Email,PhoneNumber, Events)
         */
    //public String userId;
    //public int eventNumber;
    //public String Name;
    //public String Email;
    //public String PhoneNumber;
    //public String[] Events;


    DatabaseReference mDatabase;

    public User(String userId) {
        this.userId = userId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("data");
        this.updateUserInfo();
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }



    private void updateUserInfo() {

        mDatabase.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                Name = task.getResult().child("Name").toString();
            }
        });
    }





    public boolean AddEvent(String eventName, String eventDescription, String eventDate) {
        DatabaseReference usersRef = mDatabase.child("users").child(this.userId).child("Events");
        Map<String, User> Events = new HashMap<>();
        Events.put(eventName, new User(eventDate));
        return true;
    }

}