package com.example.numad21s_firebase_team_puzzlers.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by aniru on 2/18/2017.
 */
@IgnoreExtraProperties
public class User implements Serializable {
    public String username;

    // Don't remove, This is required for
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
