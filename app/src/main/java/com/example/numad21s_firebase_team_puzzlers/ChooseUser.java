package com.example.numad21s_firebase_team_puzzlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChooseUser extends AppCompatActivity {

    private ListView chooseListView;
    private ArrayList<User> users = new ArrayList();
    private ArrayAdapter<User> adapter;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        // Cache UI elements
        chooseListView = findViewById(R.id.UserListView);

        // Hook list items onClick with StartMessenger(...)
        chooseListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            StartMessenger(users.get(position));
        });

        // Bind users with UI elements
        adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, users);
        chooseListView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null)
                    return;

                users.add(user);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    // onClick handler for user buttons
    public void StartMessenger(User targetUser) {
        Intent msgIntent = new Intent(this, MessagingActivity.class);
        msgIntent.putExtra("currentUser", currentUser);
        msgIntent.putExtra("targetUser", targetUser);

        startActivity(msgIntent);
    }
}