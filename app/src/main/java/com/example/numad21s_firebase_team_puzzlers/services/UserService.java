package com.example.numad21s_firebase_team_puzzlers.services;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numad21s_firebase_team_puzzlers.R;
import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserService {
    /**
     * Binds user data into the provided userLayout View.
     */
    public static void bindUsersToLayout(FirebaseDatabase db, Context context, LinearLayout userLayout) {
        DatabaseReference userRef = db.getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    User retrievedUser = messageSnapshot.getValue(User.class);

                    TextView textView = new TextView(context);
                    textView.setText(retrievedUser.username);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));

                    userLayout.addView(textView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Creates & pushes a new user into Firebase.
     * @return Returns the new Firebase entry ID.
     */
    public static String createNewUser(FirebaseDatabase db, String username) {
        DatabaseReference dbRef = db.getReference();
        DatabaseReference newUserRef = dbRef.child("users").push();

        User user = new User(username);
        newUserRef.setValue(user);

        String postID = newUserRef.getKey();
        System.out.println("[UserService] Created new user with username: " + username);

        return postID;
    }
}
