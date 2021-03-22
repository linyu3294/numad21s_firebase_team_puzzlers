package com.example.numad21s_firebase_team_puzzlers.services;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.numad21s_firebase_team_puzzlers.HomeActivity;
import com.example.numad21s_firebase_team_puzzlers.LoginActivity;
import com.example.numad21s_firebase_team_puzzlers.R;
import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class UserService {
    /**
     * Binds user data into the provided userLayout View.
     */
    public static void bindUsersToLayout(FirebaseDatabase db, HomeActivity homeActivity, LinearLayout userLayout) {
        DatabaseReference userRef = db.getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    User retrievedUser = messageSnapshot.getValue(User.class);

                    Button userBtn = new Button(homeActivity);
                    userBtn.setText(retrievedUser.username);
                    userBtn.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));

                    // handle message user button
                    userBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            homeActivity.StartMessagingUser(retrievedUser);
                        }
                    });

                    userLayout.addView(userBtn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Creates & pushes a new user into Firebase.
     *
     * @return Returns the new Firebase entry ID.
     */
    public static User createNewUser(FirebaseDatabase db, String username, String msgToken) {
        DatabaseReference dbRef = db.getReference();
        DatabaseReference newUserRef = dbRef.child("users").push();
        User user = new User(username, msgToken);
        newUserRef.setValue(user);

        String postID = newUserRef.getKey();
        System.out.println("[UserService] Created new user with username: " + username);

        return user;
    }
}