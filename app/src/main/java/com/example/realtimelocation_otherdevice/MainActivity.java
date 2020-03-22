package com.example.realtimelocation_otherdevice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
   EditText email, password;
   Button login;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        login = findViewById(R.id.button);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!= null){
                    Toast.makeText(MainActivity.this, "You are Logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, ListOnline.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailid = email.getText().toString();
                String mpassword = password.getText().toString();
                if(emailid.isEmpty()){
                    email.setError("Please enter Email id");
                    email.requestFocus();
                }
                else if(mpassword.isEmpty()){
                    password.setError("Please enter password");
                    password.requestFocus();
                }
                else if(emailid.isEmpty() && mpassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(emailid.isEmpty() && mpassword.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(emailid, mpassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Error Occurred, Please try again", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                    Intent in = new Intent(MainActivity.this, ListOnline.class);
                                    in.putExtra("email", emailid);
                                    startActivity(in);
                                    finish();

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }}