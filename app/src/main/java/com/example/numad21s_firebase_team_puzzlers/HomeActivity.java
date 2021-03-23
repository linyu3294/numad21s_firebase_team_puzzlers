package com.example.numad21s_firebase_team_puzzlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private FirebaseDatabase db;

    private TextView welcomeMsg;
    private User currentUser;
    private LinearLayout userLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Cache UI elements
        welcomeMsg = findViewById(R.id.txt_welcome);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        userLayout = findViewById(R.id.scroll_users_view_layout);

        db = FirebaseDatabase.getInstance();

        welcomeMsg.setText("Hello, " + currentUser.getUsername() + " !");

        // TODO: this is placeholder, we need to improve the UI rendering of user list
        // Bind user data into a list of buttons
        db.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // For each user model
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    User user = messageSnapshot.getValue(User.class);
                    if (user == null)
                        continue;

                    // Create a Button with the users' username
                    Button userBtn = new Button(getApplicationContext());
                    userBtn.setText(user.username);
                    userBtn.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));

                    // Hook onClick button
                    userBtn.setOnClickListener(v -> StartMessenger(user));

                    userLayout.addView(userBtn);
                }
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