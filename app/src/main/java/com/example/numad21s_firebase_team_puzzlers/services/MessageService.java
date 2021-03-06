package com.example.numad21s_firebase_team_puzzlers.services;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad21s_firebase_team_puzzlers.model.Message;
import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessageService {
    /**
     * Creates & pushes a new message into Firebase.
     */
    public static Message createNewMessage(FirebaseDatabase db, User userFrom, User userTo, int emojiID) {
        DatabaseReference dbRef = db.getReference();
        DatabaseReference newMsgRef = dbRef.child("messages").push();

        Message newMsg = new Message(userFrom, userTo, emojiID);
        newMsgRef.setValue(newMsg);

        String postID = newMsgRef.getKey();
        System.out.println("[MessageService] Created new message from "
                + newMsg.userFrom.username + " to "
                + newMsg.userTo.username + " with emojiID:"
                + newMsg.emojiID);

        return  newMsg;
    }
}
