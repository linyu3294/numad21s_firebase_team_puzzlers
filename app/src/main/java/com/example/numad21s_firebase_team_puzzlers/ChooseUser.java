package com.example.numad21s_firebase_team_puzzlers;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseUser extends AppCompatActivity {

    private ListView chooseListView;
    private ArrayList users;
    private ArrayList keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        chooseListView = (ListView) findViewById(R.id.UserListView);
        users = new ArrayList();
        keys = new ArrayList();

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        chooseListView.setAdapter(adapter);

//        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println(dataSnapshot.child("username"));
                String username = dataSnapshot.child("username").getValue().toString();
                users.add(username);


                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key =  childSnapshot.getKey();
                    System.out.println("This is the key for error" + key);
                    keys.add(key);
                }
                adapter.notifyDataSetChanged();

//                System.out.println("Previous Post ID: " + prevChildKey);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

        });

        chooseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> messages = new HashMap<>();
                messages.put("from", "");
                messages.put("emoji", "");
                messages.put("message", "");
                FirebaseDatabase.getInstance().getReference().child("users").child((String) keys.get(position)).child("messages").push().setValue(messages);
            }
        });
    }
}