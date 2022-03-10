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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Personalaccount extends AppCompatActivity {

    private EditText name_text,email_text,password_text;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button btn_editname;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personalaccount);
        name_text = (EditText) findViewById(R.id.namepersonal);
        email_text = (EditText) findViewById(R.id.emailpersonal);
        password_text = (EditText) findViewById(R.id.passwordpersonal);
        //Intent intent
           //     = new Intent(Personalaccount.this,
            //    MainActivity.class);
        //startActivity(intent);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        getdata(user.getUid());
        //btn_editname.setOnClickListener(this);
    }
    public void onClick(View view){
        if(view == btn_editname){
        }
    }

    private void getdata(String userId) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                    //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
                    //String name = task.getResult().child("Name").getValue().toString();
                   // Log.i("NAME", name);
                    name_text.setText(name);
                    email_text.setText(email);
                    password_text.setText(password);
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