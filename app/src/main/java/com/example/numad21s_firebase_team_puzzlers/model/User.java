package com.example.numad21s_firebase_team_puzzlers.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by aniru on 2/18/2017.
 */
@IgnoreExtraProperties
public class User {
    public String username;
    public String id;

    // Don't remove, This is required for
    public User(){}

    public User(String username){
        this.username = username;
    }

    public User(String username, String id){
        this.username = username;
        this.id = id;
    }

}
