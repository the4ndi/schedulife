package com.iifratres.schedulife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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


public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signin;
    private EditText email_text,password_text;
    private TextView signup_text;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_signin = (Button) findViewById(R.id.login);
        email_text = (EditText) findViewById(R.id.emaillogin);
        password_text = (EditText) findViewById(R.id.passwordlogin);
        signup_text = (TextView) findViewById(R.id.signuplogin);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                }
                else{
                    startActivity(new Intent(SignIn.this, MainActivity.class));
                }
            }
        };

        btn_signin.setOnClickListener(this);
        signup_text.setOnClickListener(this);


    }

    public void onClick(View view){
        if(view == btn_signin){
            loginUserAccount();
        } else if (view == signup_text) {
            Intent intent
                    = new Intent(this,
                    SignUp.class);
            startActivity(intent);
        }
    }


    public void loginUserAccount()
    {
        String email, password;
        email = email_text.getText().toString();
        password = password_text.getText().toString();
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
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Intent intent
                                            = new Intent(SignIn.this,
                                            //Personalaccount.class);
                                            MainCreateEvent.class);
                                    startActivity(intent);
                                }
                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Login failed!!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
    }





}