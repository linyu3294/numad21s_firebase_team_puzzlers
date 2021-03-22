package com.example.numad21s_firebase_team_puzzlers.services;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numad21s_firebase_team_puzzlers.model.Message;
import com.example.numad21s_firebase_team_puzzlers.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageService {
    /**
     * Binds message data into the provided messageLayout View.
     */
    public static void bindMessagesToLayout(FirebaseDatabase db, Context context, LinearLayout msgLayout, User user1, User user2) {
        DatabaseReference msgsRef = db.getReference("messages");

        msgsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message msg = messageSnapshot.getValue(Message.class);

                    // Filter unrelated messages
                    if ((msg.userFrom == user1 && msg.userTo == user2) ||
                            msg.userFrom == user2 && msg.userTo == user1) {

                        // TODO: render emoji
                        TextView textView = new TextView(context);
                        textView.setText(msg.emojiID);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        ));

                        msgLayout.addView(textView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Creates & pushes a new message into Firebase.
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
