package com.iifratres.schedulife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signin;
    private EditText name_text,email_text,password_text;
    private TextView signup_text;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_signin = (Button) findViewById(R.id.signup);
        name_text = (EditText) findViewById(R.id.namesignup);
        email_text = (EditText) findViewById(R.id.emailsignup);
        password_text = (EditText) findViewById(R.id.passwordsignup);
        signup_text = (TextView) findViewById(R.id.loginsignup);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                }
                else{
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                }
            }
        };

        btn_signin.setOnClickListener(this);
        signup_text.setOnClickListener(this);


    }

    public void onClick(View view){
        if(view == btn_signin){
            registerNewUser();
        } else if (view == signup_text) {
            Intent intent
                    = new Intent(SignUp.this,
                    SignIn.class);
            startActivity(intent);
        }
    }

    private Boolean validateName(){
        String val = name_text.getText().toString();

        if (val.isEmpty()){
            name_text.setError("Name is Empty");
            return false;
        }
        else if(val.length()>=15){
            name_text.setError("Name is too long!");
            return false;
        }
        else {
            name_text.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = email_text.getText().toString();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()){
            email_text.setError("Email is Empty");
            return false;
        }
        else if(val.length()>=35){
            email_text.setError("Email is too long!");
            return false;
        }
        else if(!val.matches(emailPattern)){
            email_text.setError("Invalid email address");
            return false;
        }
        else {
            email_text.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password_text.getText().toString();
        String passwordPattern = "^" +
                "(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[!@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";
        if (val.isEmpty()){
            password_text.setError("Password is Empty");
            return false;
        }
        //else if(val.length()>=35){
        //    password_text.setError("Password is too long!");
        //    return false;
        //}
        else if(!val.matches(passwordPattern)){
            password_text.setError("Make sure your password has atleast 1 digit, 1 lower case letter, 1 upper case letter, 1 special character, no white space, and atleast 4 characters");
            return false;
        }
        else {
            password_text.setError(null);
            return true;
        }
    }

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        //progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String name,email, password;
        name = name_text.getText().toString();
        email = email_text.getText().toString().trim();
        password = password_text.getText().toString();

        if(!validateName() | !validateEmail() | !validatePassword()){
            return;
        }
        /*
        // Validations for input email and password
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter name!",
                    Toast.LENGTH_LONG)
                    .show();
            return;

        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        */

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("firebase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("users").child(user.getUid()).child("Name").setValue(name_text.getText().toString());
                            mDatabase.child("users").child(user.getUid()).child("Email").setValue(email_text.getText().toString());
                            mDatabase.child("users").child(user.getUid()).child("Password").setValue(password_text.getText().toString());
                            Intent intent
                                   = new Intent(SignUp.this,
                                   Personalaccount.class);
                            startActivity(intent);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("firebase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }
}