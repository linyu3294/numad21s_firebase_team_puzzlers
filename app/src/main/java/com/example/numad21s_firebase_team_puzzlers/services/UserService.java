package com.example.numad21s_firebase_team_puzzlers.services;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserService {
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
