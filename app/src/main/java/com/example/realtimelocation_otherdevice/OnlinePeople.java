package com.example.realtimelocation_otherdevice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.Edits;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class OnlinePeople extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<String> onlineUsers;
    ArrayList<String> UsersName;
    ArrayList<String> onlineUserName;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_people);
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        listView = findViewById(R.id.listView);
        onlineUsers = new ArrayList<>();
        UsersName = new ArrayList<>();
        onlineUserName = new ArrayList<>();
        final ArrayAdapter adr = new ArrayAdapter(this, android.R.layout.simple_list_item_1, onlineUserName);
        listView.setAdapter(adr);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                onlineUsers.clear();
                onlineUserName.clear();
                UsersName.clear();
                for(DataSnapshot uids: dataSnapshot.getChildren()){
                    String value = uids.child("connections").child("status").getValue().toString();
                    onlineUsers.add(value);
                    String mail = uids.child("connections").child("mail").getValue().toString();
                    UsersName.add(mail);
                    String status = mail+" is "+value;
                    onlineUserName.add(status);
                    adr.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
