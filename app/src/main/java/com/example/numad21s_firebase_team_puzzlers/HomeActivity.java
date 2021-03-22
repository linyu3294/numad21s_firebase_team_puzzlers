package com.example.numad21s_firebase_team_puzzlers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.example.numad21s_firebase_team_puzzlers.services.EmojiService;
import com.example.numad21s_firebase_team_puzzlers.services.UserService;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    private TextView welcomeMsg;
    private List<User> otherUsers;
    private String myUserName;
    private String myInstanceId;
    private User myUserInstance;
    private LinearLayout userLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseDatabase.getInstance();
        myUserName = getIntent().getStringExtra("myUserName");
        welcomeMsg = (TextView) findViewById(R.id.txt_welcome);
        myInstanceId = getIntent().getStringExtra("myInstanceId");
        myUserInstance = (User) getIntent().getSerializableExtra("myUserInstance");
        userLayout = findViewById(R.id.scroll_users_view_layout);

        welcomeMsg.setText("Hello, " + myUserName + " !");
        otherUsers = new ArrayList<User>();

        UserService.bindUsersToLayout(db, this, userLayout);

        EmojiService.getAllEmoji();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("My Instance ID   " + myInstanceId);
//        db.getReference().child("users").child(myInstanceId).setValue(null);
    }

}