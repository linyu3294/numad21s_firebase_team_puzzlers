package com.example.numad21s_firebase_team_puzzlers.services;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserService {
    public void getAllUsers(FirebaseDatabase db) {
        // TODO
        // DatabaseReference userRef = db.getReference("users");
    }

    /**
     * Creates & pushes a new user into Firebase.
     *
     * @param db
     * @param username
     * @return Returns the new Firebase entry ID.
     */
    public String createNewUser(FirebaseDatabase db, String username) {
        DatabaseReference dbRef = db.getReference();
        DatabaseReference newUserRef = dbRef.child("users").push();

        User user = new User(username);
        newUserRef.setValue(user);

        String postID = newUserRef.getKey();
        System.out.println("[UserService] Created new user with username: " + username);

        return postID;
    }
}
