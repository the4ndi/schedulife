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


public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signin;
    private EditText email_text,password_text;
    private TextView signup_text;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_signin = (Button) findViewById(R.id.signup);
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

    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        //progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = email_text.getText().toString().trim();
        password = password_text.getText().toString();

        // Validations for input email and password
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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("firebase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent
                                    = new Intent(SignUp.this,
                                    MainActivity.class);
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

/*
        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                           // progressBar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(SignUp.this,
                                    MainActivity.class);
                            startActivity(intent);
                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                           // progressBar.setVisibility(View.GONE);
                        }
                    }
                });*/
    }





}