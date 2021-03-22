package com.example.numad21s_firebase_team_puzzlers.services;

import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserService {
    public void getAllUsers(FirebaseDatabase db) {
        DatabaseReference userRef = db.getReference("users");
    }

    public void createNewUser(FirebaseDatabase db, String username) {
        DatabaseReference userRef = db.getReference("users");
        userRef.get()

    }
}
