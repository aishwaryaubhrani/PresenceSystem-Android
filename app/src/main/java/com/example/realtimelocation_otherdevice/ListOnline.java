package com.example.realtimelocation_otherdevice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class ListOnline extends AppCompatActivity {
    TextView menu;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online);
        firebaseDatabase = FirebaseDatabase.getInstance();
        menu = findViewById(R.id.textView);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ListOnline.this, menu);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.join:
                                Log.i("Joiningg", "Hello");
                                manageConnections();
                                Toast.makeText(getApplicationContext(), "Joining", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.logout:
                                manageLogout();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(ListOnline.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void manageConnections(){
        b = getIntent().getExtras();
        final String mailid = b.getString("email");
        final DatabaseReference connectionReference = firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("connections");
        final DatabaseReference lastConnected = firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("connections").child("last seen");
        final DatabaseReference infoConnected = firebaseDatabase.getReference().child(".info/connected");
        infoConnected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connection_established = dataSnapshot.getValue(Boolean.class);
                if(connection_established){
                    connectionReference.child("status").setValue("online");
                    connectionReference.child("mail").setValue(mailid);
                    connectionReference.child("status").onDisconnect().setValue("offline");
                    Intent intent = new Intent(ListOnline.this, OnlinePeople.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListOnline.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manageLogout(){
        final DatabaseReference connectionReference = firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("connections");
        final DatabaseReference lastConnected = firebaseDatabase.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("connections").child("last seen");
        final DatabaseReference infoConnected = firebaseDatabase.getReference().child(".info/connected");
        infoConnected.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connection_established = dataSnapshot.getValue(Boolean.class);
                if(connection_established){
                    connectionReference.child("status").setValue("offline");
                    lastConnected.setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListOnline.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
