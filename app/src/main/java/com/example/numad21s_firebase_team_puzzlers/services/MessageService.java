package com.example.numad21s_firebase_team_puzzlers.services;

import com.example.numad21s_firebase_team_puzzlers.model.Message;
import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageService {
    public static void getAllMessagesBetween(FirebaseDatabase db, User user1, User user2) {
    }

    /**
     * Creates & pushes a new message into Firebase.
     * @return
     */
    public static void createNewMessage(FirebaseDatabase db, User userFrom, User userTo, int emojiID) {
        DatabaseReference dbRef = db.getReference();
        DatabaseReference newMsgRef = dbRef.child("messages").push();

        Message newMsg = new Message(userFrom, userTo, emojiID);
        newMsgRef.setValue(newMsg);

        String postID = newMsgRef.getKey();
        System.out.println("[MessageService] Created new message from "
                + newMsg.userFrom.username + " to "
                + newMsg.userTo.username + " with emojiID:"
                + newMsg.emojiID);
    }
}
