package com.example.numad21s_firebase_team_puzzlers.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.sql.Time;

@IgnoreExtraProperties
public class Message implements Serializable {
    // TODO: store Time here, or use firebase's time feature?
    // public Time time
    public User userFrom;
    public User userTo;
    public int emojiID;

    public Message() {
        userFrom = null;
        userTo = null;
        emojiID = -1;
    }

    public Message(User userFrom, User userTo, int emojiID) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.emojiID = emojiID;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public int getEmojiID() {
        return emojiID;
    }
}
