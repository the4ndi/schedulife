package com.iifratres.schedulife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Personalaccount extends AppCompatActivity {

    private String oldemail, oldpass;
    private TextView nameTextview;
    private TextView emailTextview;
    private TextView passwordTextview;
    private TextView phoneTextview;
    private EditText nameedittext, emailedittext, passwordedittext, editTextValue;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String email, password;
    private static final String USERS = "users";
    private Button sendDatabtn, signoutbtn;

    String currentUserId;


    private Boolean validateName(){
        String val = nameedittext.getText().toString();

        if (val.isEmpty()){
            nameedittext.setError("Name is Empty");
            return false;
        }
        else if(val.length()>=15){
            nameedittext.setError("Name is too long!");
            return false;
        }
        else {
            nameedittext.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = emailedittext.getText().toString();

        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()){
            emailedittext.setError("Email is Empty");
            return false;
        }
        else if(val.length()>=35){
            emailedittext.setError("Email is too long!");
            return false;
        }
        else if(!val.matches(emailPattern)){
            emailedittext.setError("Invalid email address");
            return false;
        }
        else {
            emailedittext.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = passwordedittext.getText().toString();
        String passwordPattern = "^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[!@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if (val.isEmpty()){
            passwordedittext.setError("Password is Empty");
            return false;
        }
        //else if(val.length()>=35){
        //    password_text.setError("Password is too long!");
        //    return false;
        //}
        else if(!val.matches(passwordPattern)){
            passwordedittext.setError("Make sure your password has atleast 1 digit, 1 lower case letter, 1 upper case letter, 1 special character, no white space, and atleast 4 characters");
            return false;
        }
        else {
            passwordedittext.setError(null);
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalaccount);

        sendDatabtn = findViewById(R.id.idBtnSendData);
        signoutbtn = findViewById(R.id.signout);

        nameedittext = findViewById(R.id.namepersonal7);
        emailedittext = findViewById(R.id.emailpersonal);
        passwordedittext = findViewById(R.id.passwordpersonal);


        //database
        database = FirebaseDatabase.getInstance();
         mAuth = FirebaseAuth.getInstance();


        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = mAuth.getUid(); ////////////
        mDatabase = database.getReference();
        getdata(currentUserId);

        signoutbtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent signoutI= new Intent(Personalaccount.this, SignIn.class);
            startActivity(signoutI);
            });

        sendDatabtn.setOnClickListener(v -> {

            // getting text from our edittext fields.
            String name = nameedittext.getText().toString();
            String email = emailedittext.getText().toString();
            String password = passwordedittext.getText().toString();

            if(!validateName() | !validateEmail() | !validatePassword()){
                return;
            }
            // below line is for checking weather the
            // edittext fields are empty or not.
            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                // if the text fields are empty
                // then show the below message.
                Toast.makeText(Personalaccount.this, "Please add some data.", Toast.LENGTH_SHORT).show();
            } else {
                // else call the method to add
                // data to our database.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential(oldemail, oldpass); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            Log.d("txt", "User re-authenticated.");
                            //Now change your email address \\
                            //----------------Code for Changing Email Address----------\\
                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            user1.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("txtt", "User email address updated.");
                                            }
                                        }
                                    });
                            user1.updatePassword(password)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("txtt", "User email address updated.");
                                            }
                                        }
                                    });
                            //----------------------------------------------------------\\
                        });

                addDataFirebase(name, email, password);
                mDatabase.child("users").child(mAuth.getUid()).child("Name").setValue(name);
                mDatabase.child("users").child(mAuth.getUid()).child("Email").setValue(email);
                mDatabase.child("users").child(mAuth.getUid()).child("Password").setValue(password);
            }
        });
    }

    private void addDataFirebase(String n, String e, String p) {


        // we are use add value event listener method
        // which is called with database reference.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.

                // after adding this data we are showing toast message.
                Toast.makeText(Personalaccount.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(Personalaccount.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }




   // public void onStart(){
       // if(view == btn_editname){
      //  }
  //  }

    private void getdata(String userId) {
        // calling add value event listener method
        // for getting the values from database.

        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot dataSnapshot = task.getResult();
                    String name = String.valueOf(dataSnapshot.child("Name").getValue());
                    String email = String.valueOf(dataSnapshot.child("Email").getValue());
                    String password = String.valueOf(dataSnapshot.child("Password").getValue());
                    oldemail = email;
                    oldpass = password;
                    //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
                    //String name = task.getResult().child("Name").getValue().toString();
                    Log.e("NAME", name);
                    //nameTextview.setText(name);
                   // emailTextview.setText(email);
                    EditText txt = findViewById(R.id.namepersonal7);
                    txt.setText(name);
                    Log.e("tag",txt.getText().toString());
                    emailedittext.setHint(email);
                    //password_text.setText(password);
                }

            }
        });
    }


 /*   public class NewUser extends SignUp {

        // show the visibility of progress bar to show loading
        //progressbar.setVisibility(View.VISIBLE);
        // Take the value of two edit texts in Strings

        public String getEmail() {
            return this.email;
        }


        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            //Toast.makeText(getApplicationContext(),
             //       "Please enter name!",
                 //   Toast.LENGTH_LONG)
                   // .show();
            return email;

        }
        }
*/
}