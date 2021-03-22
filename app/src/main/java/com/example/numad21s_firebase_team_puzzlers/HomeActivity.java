package com.example.numad21s_firebase_team_puzzlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    private TextView welcomeMsg;
    private List<User> otherUsers;
    private String myUserName;
    private String myInstanceId;
    private User myUserInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseDatabase.getInstance();
        myUserName = getIntent().getStringExtra("myUserName");
        welcomeMsg = (TextView) findViewById(R.id.txt_welcome);
        myInstanceId = getIntent().getStringExtra("myInstanceId");
        myUserInstance = (User) getIntent().getSerializableExtra("myUserInstance");

        welcomeMsg.setText("Hello, " + myUserName + " !");
        otherUsers = new ArrayList<User>();

        DatabaseReference userRef = db.getReference("users");
        Context thisActivityContext = this;
        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    User retrievedUser = messageSnapshot.getValue(User.class);
                    LinearLayout outerContainer = findViewById(R.id.scroll_users_view_layout);
                    TextView textView = new TextView(thisActivityContext);
                    textView.setText(retrievedUser.username);
                    LinearLayout.LayoutParams innerContainer = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    textView.setLayoutParams(innerContainer);
                    outerContainer.addView(textView);
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("My Instance ID   " + myInstanceId);
//        db.getReference().child("users").child(myInstanceId).setValue(null);
    }

}