package com.example.numad21s_firebase_team_puzzlers.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by aniru on 2/18/2017.
 */
@IgnoreExtraProperties
public class User implements Serializable {
    public String username;
    public String messageToken;

    // Don't remove, This is required for
    public User() {
    }

    public User(String username, String messageToken) {
        this.username = username;
        this.messageToken = messageToken;
    }

    public String getUsername() {
        return username;
    }

    public String getMessageToken() {
        return messageToken;
    }

    @Override
    public String toString() {
        return username;
    }

    public boolean EqualsTo(User other)
    {
        return username.equals(other.getUsername());
    }
}
